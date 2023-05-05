package com.bhsoft.ar3d.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bhsoft.ar3d.BuildConfig
import com.bhsoft.ar3d.R
import com.bhsoft.ar3d.databinding.ActivitySettingsBinding
import com.codemybrainsout.ratingdialog.MaybeLaterCallback
import com.codemybrainsout.ratingdialog.RateButtonCallback
import com.codemybrainsout.ratingdialog.RatingDialog
import com.jakewharton.rxbinding4.view.clicks
import java.util.concurrent.TimeUnit

class SettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.lnShare.clicks().throttleFirst(1,TimeUnit.SECONDS).subscribe{
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.type = "text/plain"
            intent.putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.app_name) + " app for Android - https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
            )
            startActivity(Intent.createChooser(intent, "Share to"))
        }

        binding.tvMoreAPp.clicks().throttleFirst(2, TimeUnit.SECONDS).subscribe {
            val moreAppIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/developer?id=Dktech+app+publishing&hl=en")
            )
            startActivity(moreAppIntent)
        }
        binding.tvEmail.setOnClickListener() {
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.data = Uri.parse("mailto:")
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("huytu180401@gmail.com"))
            emailIntent.putExtra(
                Intent.EXTRA_SUBJECT,
                "Feedback App " + getString(R.string.app_name)
            )
            startActivity(Intent.createChooser(emailIntent, "Send email..."))
        }

        binding.lnRate.setOnClickListener {
            showRate()
        }

    }

    override fun onResume() {
        super.onResume()
    }

    fun showRate() {
        val ratingDialog: RatingDialog = RatingDialog.Builder(this)
            .session(1)
            .date(1)
            .setNameApp(getString(R.string.app_name))
            .setIcon(R.drawable.logo_dhcnhn)
            .setEmail("dktechincsp@gmail.com")
            .setOnlickRate(RateButtonCallback { rate ->

            })
            .ignoreRated(false)
            .isShowButtonLater(true)
            .isClickLaterDismiss(true)
            .setTextButtonLater("Maybe Later")
            .setOnlickMaybeLate(MaybeLaterCallback {

            })
            .ratingButtonColor(R.color.blue)
            .build()
        ratingDialog.setCanceledOnTouchOutside(false)
        ratingDialog.show()
    }
}