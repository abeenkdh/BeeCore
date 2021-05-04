package com.beestudio.beecore.ads

import android.annotation.SuppressLint
import android.content.Context
import android.util.DisplayMetrics
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.beestudio.beecore.R
import com.beestudio.beecore.ads.AdsHelpers.Companion.ADMOB_BANNER_ID
import com.beestudio.beecore.ads.AdsHelpers.Companion.ADMOB_INTERSTITIAL_ID
import com.beestudio.beecore.ads.AdsHelpers.Companion.ADMOB_NATIVE_ID
import com.beestudio.beecore.ads.AdsHelpers.Companion.FACEBOOK_BANNER_ID
import com.beestudio.beecore.ads.AdsHelpers.Companion.FACEBOOK_INTERSTITIAL_ID
import com.beestudio.beecore.ads.AdsHelpers.Companion.FACEBOOK_NATIVE_ID
import com.beestudio.beecore.ads.AdsHelpers.Companion.provider
import com.beestudio.beecore.beeLogger
import com.facebook.ads.*
import com.facebook.ads.AdError
import com.facebook.ads.AdSize
import com.google.android.gms.ads.*
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import java.util.*
import kotlin.collections.ArrayList


class AdsHelpers {
    companion object {
        lateinit var unifiedNativeAd: UnifiedNativeAd
        lateinit var facebookNativeAd: NativeAd
        var provider: String = "admob"
        var isNativeLoaded: Boolean = false


        lateinit var ADMOB_BANNER_ID: String
        lateinit var FACEBOOK_BANNER_ID: String

        lateinit var FACEBOOK_NATIVE_ID: String
        lateinit var ADMOB_NATIVE_ID: String

        lateinit var FACEBOOK_INTERSTITIAL_ID: String
        lateinit var ADMOB_INTERSTITIAL_ID: String

        lateinit var ADMOB_OPEN_ADS_ID: String
    }
}

lateinit var adViewFacebook: com.facebook.ads.AdView
lateinit var adViewAdmob: AdView
lateinit var interstitialAdAdmob: InterstitialAd
lateinit var interstitialAdFacebook: com.facebook.ads.InterstitialAd

@SuppressLint("MissingPermission")
fun Context.createInterstitialAds(){
    interstitialAdAdmob = InterstitialAd(this)
    interstitialAdAdmob.adUnitId = ADMOB_INTERSTITIAL_ID
    interstitialAdAdmob.loadAd(AdRequest.Builder().build())
    interstitialAdAdmob.adListener = object : AdListener() {
        override fun onAdFailedToLoad(p0: LoadAdError?) {
            super.onAdFailedToLoad(p0)
            interstitialAdAdmob.loadAd(AdRequest.Builder().build())
        }
    }
}

var INTERSTITIAL_COUNT = 0
private var INTERSTITIAL_CLICK = 0

fun Context.showInterstitialWithCount(count : Int, block: () -> Unit){
    INTERSTITIAL_CLICK = count
    INTERSTITIAL_COUNT++
    if(INTERSTITIAL_CLICK == INTERSTITIAL_COUNT){
        INTERSTITIAL_COUNT = 0
        showInterstitialAds {
            block.invoke()
        }
    } else {
        block.invoke()
    }
}

@SuppressLint("MissingPermission")
fun Context.showInterstitialAds(block: () -> Unit){
    if(provider.toLowerCase(Locale.getDefault()) == "facebook") {
        interstitialAdFacebook = com.facebook.ads.InterstitialAd(this, FACEBOOK_INTERSTITIAL_ID)
        val interstitialAdListener = object : InterstitialAdListener {
            override fun onInterstitialDisplayed(p0: Ad?) {
            }

            override fun onAdClicked(p0: Ad?) {
            }

            override fun onInterstitialDismissed(p0: Ad?) {
                block.invoke()
            }

            override fun onError(p0: Ad?, p1: AdError) {
                block.invoke()
            }

            override fun onAdLoaded(p0: Ad?) {
                if(interstitialAdFacebook.isAdLoaded){
                    interstitialAdFacebook.show()
                }
            }

            override fun onLoggingImpression(p0: Ad?) {
            }
        }
        interstitialAdFacebook.loadAd(interstitialAdFacebook.buildLoadAdConfig()
            .withAdListener(interstitialAdListener)
            .build())
    } else {
        interstitialAdAdmob.adListener = object : AdListener() {
            override fun onAdFailedToLoad(p0: LoadAdError?) {
                super.onAdFailedToLoad(p0)
                interstitialAdAdmob.loadAd(AdRequest.Builder().build())
                block.invoke()
            }

            override fun onAdClosed() {
                super.onAdClosed()
                interstitialAdAdmob.loadAd(AdRequest.Builder().build())
                block.invoke()
            }
        }
        if(interstitialAdAdmob.isLoaded) interstitialAdAdmob.show()
    }

}

@SuppressLint("MissingPermission")
fun Context.createBannerAds(block : (View) -> Unit){
    if(provider.toLowerCase(Locale.getDefault()) == "facebook") {
        adViewFacebook = com.facebook.ads.AdView(this, FACEBOOK_BANNER_ID, AdSize.BANNER_HEIGHT_50)
        block.invoke(adViewFacebook)
        adViewFacebook.loadAd()
    } else {
        adViewAdmob = AdView(this)
        adViewAdmob.adSize = getAdSize()
        adViewAdmob.adUnitId = ADMOB_BANNER_ID
        block.invoke(adViewAdmob)
        adViewAdmob.loadAd(AdRequest.Builder().build())
    }
}

fun destroyBannerAds(){
    if(provider.toLowerCase(Locale.getDefault()) == "facebook") {
        adViewFacebook.destroy()
    } else {
        adViewAdmob.destroy()
    }
}

fun Context.getAdSize(): com.google.android.gms.ads.AdSize? {
    val context = this as AppCompatActivity
    val display: Display = context.windowManager.defaultDisplay
    val outMetrics = DisplayMetrics()
    display.getMetrics(outMetrics)
    val widthPixels = outMetrics.widthPixels.toFloat()
    val density = outMetrics.density
    val adWidth = (widthPixels / density).toInt()
    return getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
}

@SuppressLint("InflateParams")
fun Context.makeAdsView(): View {
    val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    if(provider.toLowerCase(Locale.getDefault()) == "facebook"){
        val adView = inflater.inflate(R.layout.native_view_facebook, null) as LinearLayout
        createFacebookNativeView(AdsHelpers.facebookNativeAd, adView)
        return adView
    } else {
        val adView = inflater.inflate(R.layout.native_view, null) as UnifiedNativeAdView
        createAdmobNativeView(AdsHelpers.unifiedNativeAd, adView)
        return adView
    }
}

@SuppressLint("InflateParams")
fun Context.createAdsView(view: FrameLayout){
    val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    if(provider.toLowerCase(Locale.getDefault()) == "facebook"){
        val adView = inflater.inflate(R.layout.native_view_facebook, null) as LinearLayout
        createFacebookNativeView(AdsHelpers.facebookNativeAd, adView)
        view.removeAllViews()
        view.addView(adView)
    } else {
        val adView = inflater.inflate(R.layout.native_view, null) as UnifiedNativeAdView
        createAdmobNativeView(AdsHelpers.unifiedNativeAd, adView)
        view.removeAllViews()
        view.addView(adView)
    }
}
fun Context.loadNativeAds(){
    if(provider.toLowerCase(Locale.getDefault()) == "facebook"){
        loadFacebookNative()
    } else {
        loadAdmobNative()
    }
}

@SuppressLint("MissingPermission")
private fun Context.loadAdmobNative() {
    val builder = AdLoader.Builder(this, ADMOB_NATIVE_ID) //"/6499/example/native"
    builder.forUnifiedNativeAd {
        AdsHelpers.unifiedNativeAd = it
    }
    val adLoader = builder.withAdListener(object : AdListener() {
        override fun onAdLoaded() {
            super.onAdLoaded()
            AdsHelpers.isNativeLoaded = true
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            AdsHelpers.isNativeLoaded = false
        }
    }).build()
    adLoader.loadAd(AdRequest.Builder().build())
}

private fun Context.loadFacebookNative() {
    val nativeAd = NativeAd(this, FACEBOOK_NATIVE_ID)
    nativeAd.loadAd(
        nativeAd.buildLoadAdConfig()
            .withAdListener(object : NativeAdListener {
                override fun onAdClicked(p0: Ad?) {
                }

                override fun onMediaDownloaded(p0: Ad?) {
                }

                override fun onError(p0: Ad?, p1: AdError) {
                    AdsHelpers.isNativeLoaded = false
                    beeLogger(p1.errorMessage)
                }

                override fun onAdLoaded(p0: Ad?) {
                    AdsHelpers.facebookNativeAd = nativeAd
                    AdsHelpers.isNativeLoaded = true
                    beeLogger("Facebook loaded")
                }

                override fun onLoggingImpression(p0: Ad?) {
                }
            })
            .build())
}

 fun createFacebookNativeView(nativeAd: NativeAd, adView: LinearLayout){
    nativeAd.unregisterView()
    val nativeAdIcon: com.facebook.ads.MediaView = adView.findViewById(R.id.native_ad_icon)
    val nativeAdTitle: TextView = adView.findViewById(R.id.native_ad_title)
    val nativeAdMedia: com.facebook.ads.MediaView = adView.findViewById(R.id.native_ad_media)
    val nativeAdSocialContext: TextView = adView.findViewById(R.id.native_ad_social_context)
    val nativeAdBody: TextView = adView.findViewById(R.id.native_ad_body)
    val sponsoredLabel: TextView = adView.findViewById(R.id.native_ad_sponsored_label)
    val nativeAdCallToAction: Button = adView.findViewById(R.id.native_ad_call_to_action)

    nativeAdTitle.text = nativeAd.advertiserName
    nativeAdBody.text = nativeAd.adBodyText
    nativeAdSocialContext.text = nativeAd.adSocialContext
    nativeAdCallToAction.visibility = if (nativeAd.hasCallToAction()) View.VISIBLE else View.INVISIBLE
    nativeAdCallToAction.text = nativeAd.adCallToAction
    sponsoredLabel.text = nativeAd.sponsoredTranslation
    val clickableViews: MutableList<View> = ArrayList()
    clickableViews.add(nativeAdTitle)
    clickableViews.add(nativeAdCallToAction)
    nativeAd.registerViewForInteraction(
        adView, nativeAdMedia, nativeAdIcon, clickableViews
    )
}

fun createAdmobNativeView(nativeAd: UnifiedNativeAd, adView: UnifiedNativeAdView){
    adView.mediaView = adView.findViewById(R.id.ad_media)
    adView.headlineView = adView.findViewById(R.id.ad_headline)
    adView.bodyView = adView.findViewById(R.id.ad_body)
    adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
    adView.iconView = adView.findViewById(R.id.ad_app_icon)
    adView.priceView = adView.findViewById(R.id.ad_price)
    adView.starRatingView = adView.findViewById(R.id.ad_stars)
    adView.storeView = adView.findViewById(R.id.ad_store)
    adView.advertiserView = adView.findViewById(R.id.ad_advertiser)
    (adView.headlineView as TextView).text = nativeAd.headline
    adView.mediaView.setMediaContent(nativeAd.mediaContent)

    if (nativeAd.body == null) {
        adView.bodyView.visibility = View.GONE
    } else {
        adView.bodyView.visibility = View.VISIBLE
        (adView.bodyView as TextView).text = nativeAd.body
    }

    if (nativeAd.callToAction == null) {
        adView.callToActionView.visibility = View.GONE
    } else {
        adView.callToActionView.visibility = View.VISIBLE
        (adView.callToActionView as Button).text = nativeAd.callToAction
    }

    if (nativeAd.icon == null) {
        adView.iconView.visibility = View.GONE
    } else {
        (adView.iconView as ImageView).setImageDrawable(
            nativeAd.icon.drawable
        )
        adView.iconView.visibility = View.VISIBLE
    }

    if (nativeAd.price == null) {
        adView.priceView.visibility = View.GONE
    } else {
        adView.priceView.visibility = View.VISIBLE
        (adView.priceView as TextView).text = nativeAd.price
    }

    if (nativeAd.store == null) {
        adView.storeView.visibility = View.GONE
    } else {
        adView.storeView.visibility = View.VISIBLE
        (adView.storeView as TextView).text = nativeAd.store
    }

    if (nativeAd.starRating == null) {
        adView.starRatingView.visibility = View.GONE
    } else {
        (adView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
        adView.starRatingView.visibility = View.VISIBLE
    }

    if (nativeAd.advertiser == null) {
        adView.advertiserView.visibility = View.GONE
    } else {
        (adView.advertiserView as TextView).text = nativeAd.advertiser
        adView.advertiserView.visibility = View.VISIBLE
    }
    adView.setNativeAd(nativeAd)
}