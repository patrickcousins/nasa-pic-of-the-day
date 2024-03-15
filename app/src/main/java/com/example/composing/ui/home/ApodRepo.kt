package com.example.composing.ui.home

import com.example.composing.app.logging.debug
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Inject


class ApodRepo @Inject constructor(
    retrofit: Retrofit,
    private val apiKey: String,
) {

    private val endpoint = retrofit.create<ApodEndpoint>()

    suspend fun get(spec: LoadApodSpec): List<Apod> {
        debug("loading endpoint")
        return endpoint.getApod(
            apiKey,
            count = spec.count
        )
    }


    /**
     * Query Parameters
     * Parameter	Type	Default	Description
     * date	YYYY-MM-DD	today	The date of the APOD image to retrieve
     * start_date	YYYY-MM-DD	none	The start of a date range, when requesting date for a range of dates. Cannot be used with date.
     * end_date	YYYY-MM-DD	today	The end of the date range, when used with start_date.
     * count	int	none	If this is specified then count randomly chosen images will be returned. Cannot be used with date or start_date and end_date.
     * thumbs	bool	False	Return the URL of video thumbnail. If an APOD is not a video, this parameter is ignored.
     * api_key	string	DEMO_KEY	api.nasa.gov key for expanded usage
     */
    data class LoadApodSpec(
        val date: String? = null,
        val startDate: String? = null,
        val endDate: String? = null,
        val count: Int? = null,
        val thumbs: Boolean? = null,
    )

}