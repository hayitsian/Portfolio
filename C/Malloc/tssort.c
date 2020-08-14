#include <stdio.h>
#include <stdlib.h>
#include <sys/mman.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/wait.h>
#include <unistd.h>
#include <fcntl.h>
#include <math.h>
#include <assert.h>
#include <pthread.h>

#include "float_vec.h"
#include "barrier.h"
#include "utils.h"

typedef struct sum_job {
    long r0;
    long r1;
    int pnum;
    floats* gs;
    long size;
    int T;
    floats* samps;
    long* sizes;
    barrier* bb;
    int fd;
} sum_job;

// Compare function inspired by @Luiz Fernando
// https://stackoverflow.com/questions/3886446/problem-trying-to-use-the-c-qsort-function
int
cmpfloatp(const void * a, const void * b)
{
  float fa = *(const float*) a;
  float fb = *(const float*) b;
  return (fa > fb) - (fa < fb);
}

void
qsort_floats(floats* yy)
{
    // TODO: call qsort to sort the array
    // see "man 3 qsort" for details
    int length = (int) yy->size;
    float* dat = yy->data;
    qsort(dat, length, sizeof(float), cmpfloatp);
}

void
assert_ok(long rv, char* call)
{
    if (rv == -1) {
        fprintf(stderr, "Failed call: %s\n", call);
        perror("Error:");
        exit(1);
    }
}

floats*
sample(floats* gs, long size, int T)
{
    // TODO: sample the input data, per the algorithm decription

    // - Randomly select 3*(P-1) items from the array.
    // - Sort those items.
    // - Take the median of each group of three in the sorted array, producing an array (samples) of (P-1) items.
    // - Add 0 at the start and +inf at the end (or the min and max values of the type being sorted) of the samples array so it has (P+1) items numbered (0 to P).


    long count = 3 * (T - 1);
    floats* xs = make_floats(count);

    long samps_size = T + 1;

    floats* samp = make_floats(samps_size);
    floats_push(samp, 0.0f);

    int index = 0;
    int rand_max = (int) size;
    assert(rand_max != 0);

    for(int j = 0; j < count; ++j)
    {
      index = (int) (random() % rand_max);
      floats_push(xs, gs->data[index]);
    }

    qsort_floats(xs);

    if(T > 0) {
      for(int k = 0; k < T - 1; ++k)
      {
        int index = 1 + (k * 3);
        if(index < xs->size)
        {
          float median = xs->data[index];
          floats_push(samp, median);
        }
      }
    }
    else {
      float med = xs->data[1];
      floats_push(samp, med);
    }

    floats_push(samp, 101.0f);

    free_floats(xs);

//    qsort_floats(samp);

    return samp;
}

//  sort_worker(int pnum, float* data, long size, int T, floats* samps, long* sizes, barrier* bb)
void*
sort_worker(void* arg)
{
    // TODO: select the floats to be sorted by this worker
    sum_job* job = (sum_job*) arg;
    int pnum = job->pnum;

    int size_worker = 0;
    int index = 0;

    float start = job->samps->data[pnum];
    float end = job->samps->data[pnum + 1];

    int length = (int) job->size;

    for(int z = 0; z < length; ++z)
    {
      if(start <= job->gs->data[z] && job->gs->data[z] < end)
      {
        size_worker++;
      }
      if(job->gs->data[z] >= end)
      {
        index = z - size_worker;
        break;
      }
    }

    long worker = (long) size_worker;
    floats* ys = make_floats(worker);
    job->sizes[pnum] = worker;
    float uu = 0.0f;
    for(int q = 0; q < size_worker; ++q)
    {
      uu = job->gs->data[q + index];
      floats_push(ys, uu);
    }

    printf("sample data: \n");
    floats_print(ys);

    printf("%d: start %.04f, count %ld\n", pnum, job->samps->data[pnum], ys->size);

    // TODO: some other stuff

    // Each process builds a local array of items to be sorted by scanning the full input and taking items between samples[p] and samples[p+1].
    // Write the number of items (n) taken to a shared array sizes at slot p.

    qsort_floats(ys);

    // TODO: probably more stuff

    // Copy local arrays to input.
    //
      // Each process calculates where to put its result array as follows:
      //
        // start = sum(sizes[0 to p - 1]) # that’s sum(zero items) = 0 for p = 0
        // end = sum(sizes[0 to p]) # that’s sum(1 item) for p = 0
        // Warning: Data race if you don’t synchronize here.
        // Each process copies its sorted array to input[start..end]

    int start_index = 0;
//    int end_index = (int) job->sizes[0];
    if(!(pnum == 0))
    {
      for(int y = 0; y < pnum; ++y)
      {
        start_index += (int) job->sizes[y];
//        end_index += (int) job->sizes[y+1];
      }
    }

    // these will need to be changed to prevent data races in threads
    barrier_wait(job->bb);

    int ww = job->fd;
    lseek(ww,8 + (start_index * 4), SEEK_SET);

    int wr = write(ww, job->gs->data, sizeof(float) * job->gs->size);
    assert_ok(wr, "write");

    wr = close(ww);
    assert_ok(wr, "close");

    free_floats(ys);

    // these will need to be changed to prevent data races in threads
    barrier_wait(job->bb);

    exit(0);
}

void
run_sort_workers(floats* data, long size, int T, floats* samps, long* sizes, barrier* bb, const char* output)
{
  // instead of fork(), spawn threads to utilize sharing of memory

    pthread_t threads[T];
    int rv;
    sum_job* job = malloc(sizeof(sum_job));
    job->gs = data;
    job->size = size;
    job->T = T;
    job->samps = samps;
    job->sizes = sizes;
    job->bb = bb;
    // suppress unused warning
    int ww = 0;
    // TODO: spawn P processes, each running sort_worker

    // Spawn P processes, numbered p in (0 to P-1).
    // Each process builds a local array of items to be sorted by scanning the full input and taking items between samples[p] and samples[p+1].
    // Write the number of items (n) taken to a shared array sizes at slot p.

    for (int ii = 0; ii < T; ++ii) {
      job->r0 = ii;
      job->r1 = ii + 1;
      job->pnum = ii;
      ww = open(output, O_RDWR);
      assert_ok(ww, "open");
      job->fd = ww;
      rv = pthread_create(&(threads[ii]), 0, sort_worker, job);
      assert(rv == 0);
    }

    for (int ii = 0; ii < T; ++ii) {
      rv = pthread_join(threads[ii], 0);
      assert(rv == 0);
    }
    // Terminate the P subprocesses. Array has been sorted “in place”.
}

void
sample_sort(floats* data, long size, int T, long* sizes, barrier* bb, const char* output)
{
    floats* samps = sample(data, size, T);
    pritnf("samples: \n");
    floats_print(samps);
    run_sort_workers(data, size, T, samps, sizes, bb, output);
    free_floats(samps);
}

int
main(int argc, char* argv[])
{
    alarm(120);

    if (argc != 4) {
        printf("Usage:\n");
        printf("\t%s T data.dat result.dat\n", argv[0]);
        return 1;
    }

    const int T = atoi(argv[1]);

    if (T <= 0) {
      printf("Usage: number of threads must be greater than 0\n");
      return 2;
    }

    const char* fname = argv[2];
    const char* wname = argv[3];

    seed_rng();

    int rv;
    struct stat st;
    rv = stat(fname, &st);
    check_rv(rv);

    // number of bytes in the file
    const int fsize = st.st_size;
    if (fsize < 8) {
        printf("File too small.\n");
        return 1;
    }

    int fd = open(fname, O_RDWR);
    check_rv(fd);

    // TODO: load the file with fread.
    // TODO: These should probably be from the input file.

    long int count_int = (fsize - 8) / 4;
    long* count = &count_int;

    float* file = malloc((*count) * sizeof(float));
    lseek(fd, 8, SEEK_SET);
    read(fd, file, *count * 4);

    rv = close(fd);
    assert_ok(rv, "close");

    // convert the float into a floats*
    floats* data = make_floats(*count);
    for(int i = 0; i < *count; ++i)
    {
      floats_push(data, file[i]);
    }

    long* sizes = malloc(T * sizeof(long));

    barrier* bb = make_barrier(T);

    // create and truncate the output file for use in the threads
    int wd = open(wname, O_RDWR | O_TRUNC | O_CREAT, S_IRWXU | S_IRGRP | S_IROTH);
    assert_ok(wd, "open");

    ftruncate(wd, fsize);

    rv = write(wd, count, sizeof(long));
    assert_ok(rv, "write");

    rv = close(wd);
    assert_ok(rv, "close");

    printf("data: \n");
    floats_print(data);

    sample_sort(data, *count, T, sizes, bb, wname);

    free_barrier(bb);
    free_floats(data);

    free(sizes);

    free(file);

    return 0;
}
