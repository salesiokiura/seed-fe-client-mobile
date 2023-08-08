package ke.ac.tukenya.scit.seedclient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceControl.Transaction
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController


class LandingFragment : Fragment() {


        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            val view = inflater.inflate(R.layout.fragment_landing, container, false)
            val image: ImageView = view.findViewById(R.id.imageView11)
            val image1: ImageView = view.findViewById(R.id.imageView9)
            val image2: ImageView = view.findViewById(R.id.imageView8)
            val image3: ImageView = view.findViewById(R.id.imageView10)
            val image4: ImageView = view.findViewById(R.id.imageView14)
            val image5: ImageView = view.findViewById(R.id.imageView15)
            val image6: ImageView = view.findViewById(R.id.imageView12)

            image.setOnClickListener {
                findNavController().navigate(R.id.action_landingFragment_to_loginFragment)
                val fragment = LoginFragment()


            }
            image1.setOnClickListener {
                findNavController().navigate(R.id.action_landingFragment_to_contactUsFragment)
                val fragment = ContactUsFragment()
            }
            image2.setOnClickListener {
                findNavController().navigate(R.id.action_landingFragment_to_aboutUsFragment)
                val fragment = AboutUsFragment()
            }
            image3.setOnClickListener {
                findNavController().navigate(R.id.action_landingFragment_to_FAQFragment)
                val fragment = AboutUsFragment()
            }
            image4.setOnClickListener {
                findNavController().navigate(R.id.action_landingFragment_to_mapsFragment)
                val fragment = MapsFragment()
            }
            image5.setOnClickListener {
                findNavController().navigate(R.id.action_landingFragment_to_infoFragment)
                val fragment = InfoFragment()
            }
            image6.setOnClickListener {
                findNavController().navigate(R.id.action_landingFragment_to_signupFragment)
                val fragment = SignupFragment()
            }
            return view
        }

    }


