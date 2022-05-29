package com.exfaust.core

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

//region toPersistentList

fun <Value> Array<Value>.toPersistentList(): PersistentList<Value> =
    persistentListOf(*this)

fun CharArray.toPersistentList(): PersistentList<Char> =
    persistentListOf<Char>().addAll(asList())

fun ByteArray.toPersistentList(): PersistentList<Byte> =
    persistentListOf<Byte>().addAll(asList())

fun ShortArray.toPersistentList(): PersistentList<Short> =
    persistentListOf<Short>().addAll(asList())

fun IntArray.toPersistentList(): PersistentList<Int> =
    persistentListOf<Int>().addAll(asList())

fun LongArray.toPersistentList(): PersistentList<Long> =
    persistentListOf<Long>().addAll(asList())

fun FloatArray.toPersistentList(): PersistentList<Float> =
    persistentListOf<Float>().addAll(asList())

fun DoubleArray.toPersistentList(): PersistentList<Double> =
    persistentListOf<Double>().addAll(asList())

//endregion toPersistentList

