package ke.ac.tukenya.scit.seedclient

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import android.text.method.PasswordTransformationMethod
import android.text.method.HideReturnsTransformationMethod
import android.util.Log
import android.widget.TextView
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auth = FirebaseAuth.getInstance()

        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val textView: TextView? = view.findViewById(R.id.textView2)
        textView?.setOnClickListener {
            findNavController().navigate(R.id.signupFragment)
        }

        val imageL: ImageView = view.findViewById(R.id.imageView16)  // Assuming the ID is imageView16
        emailInput = view.findViewById(R.id.tukexample)
        passwordInput = view.findViewById(R.id.example123_)

        val passwordToggle: ImageView = view.findViewById(R.id.imageView19)
        passwordToggle.setOnClickListener {
            if (passwordInput.transformationMethod is PasswordTransformationMethod) {
                passwordInput.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                passwordInput.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            passwordInput.setSelection(passwordInput.text.length)
        }

        imageL.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (isInternetAvailable()) {
                signIn(email, password)
            } else {
                Toast.makeText(context, "No internet connection available.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myEditText: EditText = view.findViewById(R.id.example123_)
        myEditText.background = null
        val myyEditText: EditText = view.findViewById(R.id.tukexample)
        myyEditText.background = null

        val textView: TextView? = view.findViewById(R.id.textView2)
        textView?.setOnClickListener {
            Log.d("Navigation", "Navigating to SignupFragment")
            findNavController().navigate(R.id.signupFragment)
        }
        val textVie1: TextView? = view.findViewById(R.id.forgot_pass)
        textVie1?.setOnClickListener {
            Log.d("Navigation", "Navigating to ForgotPasswordFragment")
            findNavController().navigate(R.id.forgotPasswordFragment)
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Authentication succeeded.", Toast.LENGTH_SHORT).show()
                    navigateToAnotherFragment()
                } else {
                    if (task.exception is FirebaseAuthInvalidUserException) {
                        // This indicates the user does not exist
                        Toast.makeText(
                            context,
                            "User doesn't exist. Please sign up first.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // This indicates the password is wrong for an existing user
                        Toast.makeText(
                            context,
                            "Incorrect password. Please try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // General error
                        Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun navigateToAnotherFragment() {
        findNavController().navigate(R.id.homeFragment)
    }
}
