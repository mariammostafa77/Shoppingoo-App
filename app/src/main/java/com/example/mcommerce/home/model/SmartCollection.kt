package com.example.mcommerce.home.model

data class SmartCollection(
    val admin_graphql_api_id: String?=null,
    val body_html: String?=null,
    val disjunctive: Boolean?=null,
    val handle: String?=null,
    val id: Long?=null,
    val image: Image?=null,
    val published_at: String?=null,
    val published_scope: String?=null,
    val rules: List<Rule>?=null,
    val sort_order: String?=null,
    val template_suffix: Any?=null,
    val title: String?=null,
    val updated_at: String?=null
)