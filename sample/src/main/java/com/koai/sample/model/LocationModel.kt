package com.koai.sample.model

import com.google.gson.annotations.SerializedName

data class LocationModel(

	@field:SerializedName("zip")
	val zip: String? = null,

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("org")
	val org: String? = null,

	@field:SerializedName("timezone")
	val timezone: String? = null,

	@field:SerializedName("regionName")
	val regionName: String? = null,

	@field:SerializedName("isp")
	val isp: String? = null,

	@field:SerializedName("query")
	val query: String? = null,

	@field:SerializedName("lon")
	val lon: Any? = null,

	@field:SerializedName("as")
	val asS: String? = null,

	@field:SerializedName("countryCode")
	val countryCode: String? = null,

	@field:SerializedName("region")
	val region: String? = null,

	@field:SerializedName("lat")
	val lat: Any? = null,

	@field:SerializedName("status")
	val status: String? = null
)
