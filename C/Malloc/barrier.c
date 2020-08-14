// Author: Nat Tuck
// CS3650 starter code

#include <stdlib.h>
#include <stdio.h>
#include <errno.h>
#include <assert.h>
#include <unistd.h>

#include "barrier.h"

barrier*
make_barrier(int nn)
{
    barrier* bb = malloc(sizeof(barrier));
    assert(bb != 0);
    pthread_cond_t cond;
    pthread_mutex_t mutex;
    pthread_mutex_init(&mutex, 0);
    pthread_cond_init(&cond, 0);
    bb->mutex = mutex;
    bb->cond = cond;
    bb->count = nn;
    bb->seen  = 0;
    return bb;
}

void
barrier_wait(barrier* bb)
{
    pthread_mutex_lock(&bb->mut_seen);
    bb->seen += 1;
    int seen = bb->seen;
    pthread_mutex_unlock(&bb->mut_seen);

    while (seen < bb->count) {
        // sleep(1);
        
        pthread_cond_wait(&bb->cond, &bb->mutex);
        // TODO: Stop waiting.
        // TODO: Don't sleep here.
//        if(seen >= bb->count)
//          break;
    }
    pthread_cond_broadcast(&bb->cond);
    pthread_mutex_unlock(&bb->mutex);
    bb->seen = 0;
}

void
free_barrier(barrier* bb)
{
    free(bb);
}
