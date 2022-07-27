package com.bhsoft.ar3d.data.model

import java.io.Serializable

data class Image(var id : String,
                 var title : String,
                 var displayName : String,
                 var size : String,
                 var duration : String?,
                 var path : String,
                 var dateAdded : String) : Serializable