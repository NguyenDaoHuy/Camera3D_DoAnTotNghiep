package com.bhsoft.ar3d.data.model

import java.io.Serializable

class Pictures(var path : String,
               var title : String,
               var sizes : Long) : Serializable, Comparable<Pictures> {
    override fun compareTo(other: Pictures): Int {
        return this.title.compareTo(other.title);
    }
}