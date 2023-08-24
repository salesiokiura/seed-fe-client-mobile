package ke.ac.tukenya.scit.seedclient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.imageView72)?.setOnClickListener {
            findNavController().navigate(R.id.bookToCollectFragment)
        }

        view.findViewById<ImageView>(R.id.imageView67)?.setOnClickListener {
            findNavController().navigate(R.id.bookSlotFragment)
        }

        view.findViewById<ImageView>(R.id.imageView70)?.setOnClickListener {
            findNavController().navigate(R.id.contactUsFragment)
        }

        view.findViewById<ImageView>(R.id.imageView63)?.setOnClickListener {
            findNavController().navigate(R.id.orderAndPayFragment)
        }

        view.findViewById<ImageView>(R.id.imageView64)?.setOnClickListener {
            findNavController().navigate(R.id.sendAndRequestFragment)
        }

        view.findViewById<ImageView>(R.id.imageView65)?.setOnClickListener {
            findNavController().navigate(R.id.devicesFragment)
        }

        view.findViewById<ImageView>(R.id.imageView62)?.setOnClickListener {
            findNavController().navigate(R.id.walletFragment)
        }

        view.findViewById<ImageView>(R.id.imageView71)?.setOnClickListener {
            findNavController().navigate(R.id.checkSlotsFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }
}
