package me.daffakurnia.android.githubusers

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import me.daffakurnia.android.githubusers.API.ApiConfig
import me.daffakurnia.android.githubusers.DataClass.DataUser
import me.daffakurnia.android.githubusers.Response.UserFollowersResponse
import me.daffakurnia.android.githubusers.databinding.FragmentFollowersBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersFragment : Fragment() {

    private lateinit var binding: FragmentFollowersBinding
    private lateinit var adapter: ListUserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(USERNAME).toString()

        binding = FragmentFollowersBinding.bind(view)

        getFollowers(username)
    }

    private fun getFollowers(username: String) {
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<UserFollowersResponse>> {

            override fun onResponse(
                call: Call<List<UserFollowersResponse>>,
                response: Response<List<UserFollowersResponse>>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        getFollowersData(responseBody)
                    }
                }
            }

            override fun onFailure(call: Call<List<UserFollowersResponse>>, t: Throwable) {
                Log.d("Failure", t.message, t)
            }
        })
    }

    private fun getFollowersData(responseBody: List<UserFollowersResponse>) {
        val userList = ArrayList<DataUser>()

        for (item in responseBody) {
            userList.add(
                DataUser(
                    item.login,
                    item.htmlUrl,
                    item.avatarUrl
                )
            )
        }
        val adapter = ListUserAdapter(userList)

        binding.apply {
            listFollowers.setHasFixedSize(true)
            listFollowers.layoutManager = LinearLayoutManager(activity)
            listFollowers.adapter = adapter
        }
    }

    companion object {
        private var USERNAME = "username"
    }
}