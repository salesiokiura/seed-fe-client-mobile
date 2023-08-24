package ke.ac.tukenya.scit.seedclient

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
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
    private lateinit var passwordToggle: ImageView
    private lateinit var confirmPasswordToggle: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()

        val view = inflater.inflate(R.layout.fragment_signup, container, false)

        // Initializing views
        emailInput = view.findViewById(R.id.editTextText2)
        passwordInput = view.findViewById(R.id.editTextTextPassword2)
        nameInput = view.findViewById(R.id.editTextText)
        mobileInput = view.findViewById(R.id.editTextPhone)
        confirmPasswordInput = view.findViewById(R.id.editTextTextPassword5)
        passwordToggle = view.findViewById(R.id.imageView41)
        confirmPasswordToggle = view.findViewById(R.id.imageView45) // Make sure this is the right ID

        val login: TextView = view.findViewById(R.id.textView4)
        login.setOnClickListener {
            navigateToAnotherFragment()
        }

        val signupButton: ImageView = view.findViewById(R.id.imageView44)
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

        // Toggle visibility for password and confirm password
        passwordToggle.setOnClickListener {
            togglePasswordVisibility(passwordInput, passwordToggle)
        }

        confirmPasswordToggle.setOnClickListener {
            togglePasswordVisibility(confirmPasswordInput, confirmPasswordToggle)
        }

        return view
    }

    private fun togglePasswordVisibility(editText: EditText, toggleIcon: ImageView) {
        if (editText.transformationMethod is PasswordTransformationMethod) {
            editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            toggleIcon.setImageResource(R.drawable.eyeicon)  // 'eye closed' icon indicating password is visible
        } else {
            editText.transformationMethod = PasswordTransformationMethod.getInstance()
            toggleIcon.setImageResource(R.drawable.eyeicon)  // 'eye open' icon indicating password is hidden
        }
        editText.setSelection(editText.text.length)
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
                        navigateToAnotherFragment()
                        clearInputFields()  // Clear fields after successful registration


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
    private fun navigateToAnotherFragment() {
        findNavController().navigate(R.id.loginFragment)
}
}
