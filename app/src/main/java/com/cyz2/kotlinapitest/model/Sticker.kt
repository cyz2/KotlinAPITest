package com.cyz2.kotlinapitest.model

import java.io.Serializable

/**
 * 皮肤 - 贴纸
 * */
data class Sticker(
    /**
     * 专辑ID，[MaterialBean.subcategory_id]
     * */
    val album_id: Long,
    val alpha: Float = 1.0f,
    val center_x: Float = 0.5f,
    val horizontal_flip: Boolean = false,
    /**
     * 原图，抽取配方需要使用，应用配方不需要使用（通过[material_id]到素材管理里面取原图）
     * */
    val image_path: String,
    /**
     * 素材ID,[MaterialBean.material_id]
     * 自定义别致的素材id为[com.meitu.meitupic.materialcenter.core.entities.SubCategorySticker.CU]
     * */
    val material_id: Long,
    val rotate: Float = 0f,
    val width_ratio: Float = 0.5f,
    val image_full_path: String?
) : Serializable

