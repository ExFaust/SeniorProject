package com.exfaust.core

interface Formatter<in Data> {
    fun format(data: Data): String
}