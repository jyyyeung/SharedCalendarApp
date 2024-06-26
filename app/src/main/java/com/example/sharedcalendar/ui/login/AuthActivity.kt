package com.example.sharedcalendar.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.sharedcalendar.MainActivity
import com.example.sharedcalendar.R
import com.google.firebase.auth.FirebaseAuth

/**
 * Adapter class for managing the authentication pages in the AuthActivity.
 */
class AuthenticationPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    private val fragmentList: ArrayList<Fragment> = ArrayList()

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
    }
}

/**
 * Activity for handling user authentication.
 *
 * @param auth The instance of FirebaseAuth used for authentication.
 */
class AuthActivity(private val auth: FirebaseAuth = FirebaseAuth.getInstance()) :
    AppCompatActivity() {
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        // Skip Authentication Page if user has already logged in
        if (currentUser != null) {
            finishAffinity()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_auth)

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)

        val pagerAdapter = AuthenticationPagerAdapter(supportFragmentManager, lifecycle)
        pagerAdapter.addFragment(LoginFragment())
        pagerAdapter.addFragment(RegisterFragment())

        viewPager.adapter = pagerAdapter
    }
}
