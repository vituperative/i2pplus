#include <jni.h>

#ifndef _Included_net_i2p_util_NativeBigInteger
#define _Included_net_i2p_util_NativeBigInteger
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jbyteArray JNICALL Java_net_i2p_util_NativeBigInteger_nativeModPow
  (JNIEnv *, jclass, jbyteArray, jbyteArray, jbyteArray);

JNIEXPORT jbyteArray JNICALL Java_net_i2p_util_NativeBigInteger_nativeModPowCT
  (JNIEnv *, jclass, jbyteArray, jbyteArray, jbyteArray);

JNIEXPORT jbyteArray JNICALL Java_net_i2p_util_NativeBigInteger_nativeModInverse
  (JNIEnv *, jclass, jbyteArray, jbyteArray);

JNIEXPORT jint JNICALL Java_net_i2p_util_NativeBigInteger_nativeJbigiVersion
  (JNIEnv *, jclass);

JNIEXPORT jint JNICALL Java_net_i2p_util_NativeBigInteger_nativeGMPMajorVersion
  (JNIEnv *, jclass);

JNIEXPORT jint JNICALL Java_net_i2p_util_NativeBigInteger_nativeGMPMinorVersion
  (JNIEnv *, jclass);

JNIEXPORT jint JNICALL Java_net_i2p_util_NativeBigInteger_nativeGMPPatchVersion
  (JNIEnv *, jclass);

#ifdef __cplusplus
}
#endif
#endif
