package jp.techacademy.yuki.naito.apiapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_web_view.*
import android.util.Log
import android.view.Gravity
import androidx.appcompat.app.AlertDialog
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import io.realm.Realm
import io.realm.RealmConfiguration
import jp.techacademy.yuki.naito.apiapp.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class WebViewActivity: AppCompatActivity(), FragmentCallback  {
    private var id = ""
    private val viewPagerAdapter by lazy { ViewPagerAdapter(this) }
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        favoriteImageViewLinkTitle.setTextSize(20F)
        favoriteImageViewLinkTitle.setGravity(Gravity.RIGHT)
        Log.d("お気に入り5", "お気に入り5")
        Log.d("お気に入り10", intent.getStringExtra(KEY_ID).toString())
        var couponUrls = CouponUrls(intent.getStringExtra(KEY_URL).toString(), intent.getStringExtra(KEY_URL).toString())
        var data = Shop(couponUrls,intent.getStringExtra(KEY_ID).toString(), intent.getStringExtra(
            KEY_IMAGE_URL).toString(), intent.getStringExtra(KEY_NAME).toString())
        webView.loadUrl(intent.getStringExtra(KEY_URL).toString())
     //   data = FavoriteShop.findBy(intent.getStringExtra(KEY_ID))!!
     /*   var favoriteShop :FavoriteShop = FavoriteShop().apply {
            data.id = intent.getStringExtra(KEY_ID).toString()
            data.name = intent.getStringExtra(KEY_NAME).toString()
            data.imageUrl = intent.getStringExtra(KEY_IMAGE_URL).toString()
            data.url = intent.getStringExtra(KEY_URL).toString()
        }*/

        //val isFavorite = FavoriteShop.findBy(intent.getStringExtra(KEY_ID).toString())!! != null
        Log.d("お気に入り12", "お気に入り12")
        favoriteImageViewLink.apply {
            setImageResource(if (FavoriteShop.findBy(intent.getStringExtra(KEY_ID).toString()) != null) R.drawable.ic_star else R.drawable.ic_star_border) // Picassoというライブラリを使ってImageVIewに画像をはめ込む
            setOnClickListener {
                if (FavoriteShop.findBy(intent.getStringExtra(KEY_ID).toString()) != null) {
                    Log.d("お気に入り11", "お気に入り11")
                    showConfirmDeleteFavoriteDialog(intent.getStringExtra(KEY_ID).toString())

                   //     (viewPagerAdapter.fragments[MainActivity.VIEW_PAGER_POSITION_API] as ApiFragment).updateView()
                        //(viewPagerAdapter.fragments[MainActivity.VIEW_PAGER_POSITION_FAVORITE] as FavoriteFragment).updateData()
                    Log.d("お気に入り13", "お気に入り13")
                } else {
                    Log.d("お気に入り12", "お気に入り12")
                    AddFavorite(data)

                }
            }
        }
    }

    companion object {
        private const val KEY_URL = "key_url"
        private const val KEY_ID = "key_id"
        private const val KEY_NAME = "key_name"
        private const val KEY_IMAGE_URL = "key_image_url"
        fun start(activity: Activity, url: String, id: String, name: String, imageURL: String) {
            var intent = Intent(activity, WebViewActivity::class.java).putExtra(KEY_URL, url)
            intent.putExtra(KEY_ID, id)
            intent.putExtra(KEY_NAME, name)
            intent.putExtra(KEY_IMAGE_URL, imageURL)

            activity.startActivity(intent)

        }
    }

    private fun showConfirmDeleteFavoriteDialog(id: String) {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_favorite_dialog_title)
            .setMessage(R.string.delete_favorite_dialog_message)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                deleteFavorite(id)
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->}
            .create()
            .show()
    }

    private fun deleteFavorite(id: String) {
        FavoriteShop.delete(id)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    fun AddFavorite(shop: Shop) { // Favoriteに追加するときのメソッド(Fragment -> Activity へ通知する)

        FavoriteShop.insert(FavoriteShop().apply {
            id = shop.id
            name = shop.name
            imageUrl = shop.logoImage
            url = if (shop.couponUrls.sp.isNotEmpty()) shop.couponUrls.sp else shop.couponUrls.pc
        })
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    override fun onClickItem(url: String, id: String, name: String, imageURL: String) {
        TODO("Not yet implemented")
    }

    override fun onAddFavorite(shop: Shop) {
        TODO("Not yet implemented")
    }

    override fun onDeleteFavorite(id: String) {
        TODO("Not yet implemented")
    }
}