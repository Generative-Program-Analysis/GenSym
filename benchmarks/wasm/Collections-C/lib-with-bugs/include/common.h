/*
 * Collections-C
 * Copyright (C) 2013-2014 Srđan Panić <srdja.panic@gmail.com>
 *
 * This file is part of Collections-C.
 *
 * Collections-C is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Collections-C is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Collections-C.  If not, see <http://www.gnu.org/licenses/>.
 */

#ifndef COLLECTIONS_C_COMMON_H
#define COLLECTIONS_C_COMMON_H

#include "mockups.h"

#ifdef ARCH_64
#define MAX_POW_TWO (((size_t) 1) << 63)
#else
#define MAX_POW_TWO (((size_t) 1) << 31)
#endif /* ARCH_64 */

typedef char  int8_t;
typedef short int16_t;
typedef int   int32_t;
typedef long  int64_t;

typedef unsigned char  uint8_t;
typedef unsigned short uint16_t;
typedef unsigned int   uint32_t;
typedef unsigned long  uint64_t;

typedef int bool;
typedef int uintptr_t;
typedef unsigned long size_t;

#define true  1
#define false 0
#define NULL  0

enum cc_stat {
    CC_OK                   = 0,

    CC_ERR_ALLOC            = 1,
    CC_ERR_INVALID_CAPACITY = 2,
    CC_ERR_INVALID_RANGE    = 3,
    CC_ERR_MAX_CAPACITY     = 4,
    CC_ERR_KEY_NOT_FOUND    = 6,
    CC_ERR_VALUE_NOT_FOUND  = 7,
    CC_ERR_OUT_OF_RANGE     = 8,

    CC_ITER_END             = 9,
};

#define CC_MAX_ELEMENTS 16777216LU

#if defined(_MSC_VER)

#define       INLINE __inline
#define FORCE_INLINE __forceinline

#else

#define       INLINE inline
#define FORCE_INLINE inline __attribute__((always_inline))

#endif /* _MSC_VER */


int cc_common_cmp_str(const void *key1, const void *key2);
int cc_common_cmp_ptr(const void *key1, const void *key2);

void *malloc(unsigned int size);
void *calloc(unsigned int nmemb, unsigned int size);
void free(void *ptr);
void *memset(void *s, int c, size_t n);
int strcmp(const char *s1, const char *s2);
void *memcpy(void *dst, const void *src, unsigned int n);
void *memmove(void *dst, const void *src, unsigned int n);
void qsort(void *base, size_t nmemb, size_t size,
    int (*compar)(const void *, const void *));

#define CC_CMP_STRING  cc_common_cmp_str
#define CC_CMP_POINTER cc_common_cmp_ptr


#endif /* COLLECTIONS_C_COMMON_H */
