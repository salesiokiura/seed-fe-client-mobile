package ke.ac.tukenya.scit.seedclient

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore

class SignupFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var nameInput: EditText
    private lateinit var mobileInput: EditText
    private lateinit var login: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()

        val view = inflater.inflate(R.layout.fragment_signup, container, false)
        val login: TextView = view.findViewById(R.id.textView4)

        val signupButton: ImageView = view.findViewById(R.id.imageView44)
        emailInput = view.findViewById(R.id.editTextText2)
        passwordInput = view.findViewById(R.id.editTextTextPassword2)
        nameInput = view.findViewById(R.id.editTextText)
        mobileInput = view.findViewById(R.id.editTextPhone)
        confirmPasswordInput = view.findViewById(R.id.editTextTextPassword5)

        login.setOnClickListener {
            navigateToAnotherFragment()
        }

        signupButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            val name = nameInput.text.toString()
            val mobile = mobileInput.text.toString()
            val confirmPassword = confirmPasswordInput.text.toString()

            if (isValidEmail(email) && isValidPassword(password, confirmPassword) && isValidName(name) && isValidMobile(mobile)) {
                signUp(email, password, name, mobile)
            }
        }

        return view
    }

    private fun navigateToAnotherFragment() {
        findNavController().navigate(R.id.loginFragment)
    }

    private fun isValidPassword(password: String, confirmPassword: String): Boolean {
        if (password != confirmPassword) {
            Toast.makeText(context, "Passwords do not match.", Toast.LENGTH_SHORT).show()
            return false
        } else if (password.length < 6) {
            Toast.makeText(context, "Password should be at least 6 characters.", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun signUp(email: String, password: String, name: String, mobile: String) {
        if (isInternetAvailable()) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        saveUserToFirestore(email, name, mobile)
                    } else {
                        val errorMessage = when (task.exception) {
                            is FirebaseAuthUserCollisionException -> "This email is already registered."
                            is FirebaseAuthWeakPasswordException -> "Please choose a stronger password."
                            else -> "Registration failed: ${task.exception?.message}"
                        }
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        clearInputFields()
                    }
                }
        } else {
            Toast.makeText(context, "No internet connection available.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearInputFields() {
        emailInput.text.clear()
        passwordInput.text.clear()
        nameInput.text.clear()
        mobileInput.text.clear()
    }

    private fun saveUserToFirestore(email: String, name: String, mobile: String) {
        val user = hashMapOf(
            "email" to email,
            "name" to name,
            "mobile" to mobile
        )

        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(auth.currentUser?.uid!!)
            .set(user)
            .addOnSuccessListener {
                Toast.makeText(context, "Registration successful.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error saving user data: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    @RequiresApi(Build.VERSION_CODES.FROYO)
    private fun isValidEmail(email: String): Boolean {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true
        }
        Toast.makeText(context, "Please enter a valid email address.", Toast.LENGTH_SHORT).show()
        return false
    }

    private fun isValidName(name: String): Boolean {
        if (name.isNotBlank()) {
            return true
        }
        Toast.makeText(context, "Please enter your name.", Toast.LENGTH_SHORT).show()
        return false
    }

    private fun isValidMobile(mobile: String): Boolean {
        if (mobile.isNotBlank()) {
            return true
        }
        Toast.makeText(context, "Please enter your mobile number.", Toast.LENGTH_SHORT).show()
        return false
    }
}
