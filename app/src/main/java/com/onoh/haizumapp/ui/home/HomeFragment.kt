package com.onoh.haizumapp.ui.home

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.onoh.haizumapp.R
import com.onoh.haizumapp.data.model.Post
import com.onoh.haizumapp.data.model.User
import com.onoh.haizumapp.ui.home.post.PostActivity
import com.onoh.haizumapp.ui.home.post.PostAdapter
import com.onoh.haizumapp.vo.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_post_home.*
import java.util.*
import java.util.Collections.reverse
import java.util.Collections.reverseOrder

class HomeFragment : Fragment() {

    companion object{
        private const val REQUEST_PERMISSION_REQUEST_CODE = 2020

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //initiate view model
        val factory = ViewModelFactory.getInstance()
        val viewModel = ViewModelProvider(this,factory)[HomeViewModel::class.java]

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        viewModel.setUser(firebaseUser?.uid)
        setUser(viewModel.getUser())
        setPost(viewModel.getPost())

        fab.setOnClickListener {
            val intent = Intent(context,PostActivity::class.java)
            startActivity(intent)
        }

    }

    private fun setPost(post: LiveData<List<Post>>) {
        startShimmerPost()
        val postAdapter = PostAdapter(requireContext())
        post.observe(viewLifecycleOwner,{
            stopShimmerPost()
            if(it.isEmpty()){
                layout_null.visibility = View.VISIBLE
            }else{
                postAdapter.setPost(it)
                postAdapter.notifyDataSetChanged()
            }
        })
        with(rv_post_home){
            val layoutManager = LinearLayoutManager(activity)
            layoutManager.reverseLayout = true
            layoutManager.stackFromEnd = true
            setLayoutManager(layoutManager)
            adapter = postAdapter
        }

    }

    private fun setUser(user: LiveData<User>) {
        startShimmerProfile()
        if (ContextCompat.checkSelfPermission(
                requireActivity(),android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
                ,REQUEST_PERMISSION_REQUEST_CODE)
        }
        getLocUser()
        user.observe(viewLifecycleOwner,{
            tv_name_user.text = it.username
            stopShimmerProfile()
        })
    }

    private fun getLocUser() {
        val locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        //now getting address from latitude and longitude

        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        var addresses:List<Address>

        LocationServices.getFusedLocationProviderClient(requireActivity())
            .requestLocationUpdates(locationRequest,object : LocationCallback(){
                override fun onLocationResult(locationResult: LocationResult?) {
                    super.onLocationResult(locationResult)
                    LocationServices.getFusedLocationProviderClient(requireActivity())
                        .removeLocationUpdates(this)
                    if (locationResult != null && locationResult.locations.size > 0){
                        val locIndex = locationResult.locations.size-1

                        val latitude = locationResult.locations[locIndex].latitude
                        val longitude = locationResult.locations[locIndex].longitude

                        addresses = geocoder.getFromLocation(latitude,longitude,1)

                        val address:String = addresses[0].getAddressLine(0)
                        tv_current_loc_home.text = address
                    }
                }
            }, Looper.getMainLooper())
    }

    private fun startShimmerProfile(){
        shimmerlayout_home.visibility = View.VISIBLE
        toolbar_home.visibility = View.GONE
        shimmerlayout_home.startShimmer()
    }

    private fun startShimmerPost(){
        shimmerlayout_post.visibility = View.VISIBLE
        shimmerlayout_post.startShimmer()
    }

    private fun stopShimmerProfile(){
        shimmerlayout_home.visibility = View.GONE
        toolbar_home.visibility = View.VISIBLE
        shimmerlayout_home.stopShimmer()
    }

    private fun stopShimmerPost(){
        shimmerlayout_post.visibility = View.GONE
        shimmerlayout_post.stopShimmer()
    }


}
