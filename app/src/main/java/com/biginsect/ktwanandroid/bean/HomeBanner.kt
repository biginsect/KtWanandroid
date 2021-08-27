package com.biginsect.ktwanandroid.bean

/**
 *@author biginsect
 *Created at 2021/5/14 17:09
 */
data class HomeBanner(var data: List<Banner>){

    data class Banner(
        var desc: String,
        var id: Long,
        var imagePath: String,
        var isVisible: Int,
        var order: Int,
        var title: String,
        var type: Int,
        var url: String
    )
}
