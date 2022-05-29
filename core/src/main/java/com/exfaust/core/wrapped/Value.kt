package com.exfaust.core.wrapped

import iss.digital.mgp.metrorouting.core.EqualityComparable
import javax.annotation.CheckReturnValue

interface Value<Self : Value<Self>> : EqualityComparable<Self> {
    @CheckReturnValue
    override fun toString(): String
}