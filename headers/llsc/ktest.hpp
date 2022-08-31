#ifndef LLSC_KTEST_HEADER
#define LLSC_KTEST_HEADER

/*
 * The implementation of KTestObject, write_uint32, write_string, kTest_toFile
 * are extracted from the KLEE symbolic virtual machine project.
 * Same as KLEE, this file is distributed under the University of Illinois Open
 * Source License. https://github.com/klee/klee/blob/master/LICENSE.TXT
 */

#ifdef __cplusplus
extern "C" {
#endif

typedef struct KTestObject KTestObject;
struct KTestObject {
  char *name;
  unsigned numBytes;
  unsigned char *bytes;
};

typedef struct KTest KTest;
struct KTest {
  /* file format version */
  unsigned version;

  unsigned numArgs;
  char **args;

  unsigned symArgvs;
  unsigned symArgvLen;

  unsigned numObjects;
  KTestObject *objects;
};

#ifdef __cplusplus
}
#endif

#define KTEST_MAGIC "KTEST"
#define KTEST_VERSION 3

inline int write_uint32(FILE *f, unsigned value) {
  unsigned char data[4];
  data[0] = value>>24;
  data[1] = value>>16;
  data[2] = value>> 8;
  data[3] = value>> 0;
  return fwrite(data, 1, 4, f)==4;
}

inline int write_string(FILE *f, const char *value) {
  unsigned len = strlen(value);
  if (!write_uint32(f, len))
    return 0;
  if (fwrite(value, len, 1, f)!=1)
    return 0;
  return 1;
}

inline int kTest_toFile(KTest *bo, const char *path) {
  FILE *f = fopen(path, "wb");
  unsigned i;

  if (!f)
  {
    std::cout << "if (!f)" << std::endl;
    goto error;
  }
  if (fwrite(KTEST_MAGIC, strlen(KTEST_MAGIC), 1, f)!=1)
  {
    std::cout << "if (fwrite(KTEST_MAGIC, strlen(KTEST_MAGIC), 1, f)!=1)" << std::endl;
    goto error;
  }
  if (!write_uint32(f, KTEST_VERSION))
  {
    std::cout << "if (!write_uint32(f, KTEST_VERSION))" << std::endl;
    goto error;
  }

  if (!write_uint32(f, bo->numArgs))
  {
    std::cout << "if (!write_uint32(f, bo->numArgs))" << std::endl;
    goto error;
  }
  for (i=0; i<bo->numArgs; i++) {
    if (!write_string(f, bo->args[i]))
    {
      std::cout << "if (!write_string(f, bo->args[i]))" << std::endl;
      goto error;
    }
  }

  if (!write_uint32(f, bo->symArgvs))
  {
    std::cout << "if (!write_uint32(f, bo->symArgvs))" << std::endl;
    goto error;
  }
  if (!write_uint32(f, bo->symArgvLen))
  {
    std::cout << "if (!write_uint32(f, bo->symArgvLen))" << std::endl;
    goto error;
  }

  if (!write_uint32(f, bo->numObjects))
  {
    std::cout << "if (!write_uint32(f, bo->numObjects))" << std::endl;
    goto error;
  }
  for (i=0; i<bo->numObjects; i++) {
    KTestObject *o = &bo->objects[i];
    if (!write_string(f, o->name))
    {
      std::cout << "if (!write_string(f, o->name))" << std::endl;
      goto error;
    }
    if (!write_uint32(f, o->numBytes))
    {
      std::cout << "if (!write_uint32(f, o->numBytes))" << std::endl;
      goto error;
    }
    if (fwrite(o->bytes, o->numBytes, 1, f)!=1)
    {
      std::cout << "if (fwrite(o->bytes, o->numBytes, 1, f)!=1)" << std::endl;
      std::cout << "i: " << i << std::endl;
      std::cout << "o->name: " << o->name << std::endl;
      std::cout << "o->numBytes: " << o->numBytes << std::endl;
      std::cout << "o->bytes: " << o->bytes << std::endl;
      goto error;
    }
  }

  fclose(f);

  return 1;
 error:
  if (f) fclose(f);

  return 0;
}

#endif /* LLSC_KTEST_H */
