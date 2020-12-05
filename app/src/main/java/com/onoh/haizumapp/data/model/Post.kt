package com.onoh.haizumapp.data.model

data class Post(
    var userId :String? ="",
    var photoProfile:String? = "",
    var name:String? = "",
    var time:String? = "",
    var viewType:String? ="",
    var text:String?="",
    var imgUrl:String?="",
    var videoUrl:String?=""
)