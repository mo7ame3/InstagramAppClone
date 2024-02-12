package com.example.instagram_app.screen

import PostData
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.instagram_app.data.CommentData
import com.example.instagram_app.data.Event
import com.example.instagram_app.data.UserData
import com.example.instagram_app.navigation.AllScreens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import java.util.UUID
import javax.inject.Inject

const val USER = "users"
const val POSTS = "posts"
const val COMMENTS = "comments"

@HiltViewModel
class InstagramViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
) : ViewModel() {
    private val signedIn = mutableStateOf(false)
    val inProgress = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)
    val popUpNotification = mutableStateOf<Event<String>?>(null)

    val refreshPostProgress = mutableStateOf(false)
    val posts = mutableStateOf<List<PostData>>(listOf())

    val searchedPosts = mutableStateOf<List<PostData>>(listOf())
    val searchPostsProgress = mutableStateOf(false)

    val postsFeed = mutableStateOf<List<PostData>>(listOf())
    val postsFeedProgress = mutableStateOf(false)

    val comments = mutableStateOf<List<CommentData>>(listOf())
    val commentsProgress = mutableStateOf(false)

    val followers = mutableStateOf(0)


    init {
        //  auth.signOut()
        val currentUser = auth.currentUser
        signedIn.value = currentUser != null
        currentUser?.uid?.let { uid ->
            getUserData(uid)
        }
    }

    fun onSignup(userName: String, email: String, password: String, navController: NavController) {
        if (userName.isEmpty() or email.isEmpty() or password.isEmpty()) {
            handleException(customMassage = "please fill in all fields")
            return
        }
        inProgress.value = true
        db.collection(USER).whereEqualTo("username", userName).get()
            .addOnSuccessListener { doucments ->

                if (doucments.size() > 0) {
                    handleException(customMassage = "UserName already exists")
                    inProgress.value = false
                } else {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                signedIn.value = true
                                createOrUpdateProfile(userName = userName)
                                navController.navigate(route = AllScreens.FeedScreen.name) {
                                    navController.popBackStack()
                                    navController.popBackStack()
                                    navController.popBackStack()
                                }
                            } else {
                                handleException(
                                    exception = task.exception,
                                    customMassage = "Signup failed"
                                )

                            }
                            inProgress.value = false
                        }
                }
            }
            .addOnFailureListener {
                handleException(exception = it, customMassage = "Signup failed")
                inProgress.value = false
            }


    }

    fun onLogin(email: String, password: String, navController: NavController) {
        if (email.isEmpty() or password.isEmpty()) {
            handleException(customMassage = "please fill in all fields")
            return
        }
        inProgress.value = true

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    signedIn.value = true
                    inProgress.value = false
                    auth.currentUser?.uid?.let { uid ->
                        getUserData(uid)
                    }
                    navController.navigate(route = AllScreens.FeedScreen.name) {
                        navController.popBackStack()
                        navController.popBackStack()
                        navController.popBackStack()
                    }
                } else {
                    handleException(exception = task.exception, customMassage = "Login failed")
                    inProgress.value = false
                }
            }
            .addOnFailureListener {
                handleException(exception = it, customMassage = "Login failed")
                inProgress.value = false
            }

    }

    fun signOut(navController: NavController) {
        auth.signOut()
        if (FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) {
            signedIn.value = false
            userData.value = null
            searchedPosts.value = listOf()
            postsFeed.value = listOf()
            comments.value = listOf()
            navController.navigate(route = AllScreens.SingUpScreen.name) {
                navController.popBackStack()
                navController.popBackStack()
                navController.popBackStack()
            }
        }
    }

    private fun createOrUpdateProfile(
        userName: String? = null,
        name: String? = null,
        bio: String? = null,
        imageUrl: String? = null
    ) {
        val uid = auth.currentUser?.uid
        val userData = UserData(
            userID = uid,
            name = name ?: userData.value?.name,
            bio = bio ?: userData.value?.bio,
            imageUrl = imageUrl ?: userData.value?.imageUrl,
            userName = userName ?: userData.value?.userName,
            following = userData.value?.following
        )
        uid?.let {
            inProgress.value = true
            db.collection(USER).document(uid).get().addOnSuccessListener {
                if (it.exists()) {
                    it.reference.update(userData.toMap()).addOnSuccessListener {
                        this.userData.value = userData
                        inProgress.value = false
                    }
                        .addOnFailureListener { ex ->
                            handleException(ex, "Can't update user")
                            inProgress.value = false
                        }
                } else {
                    db.collection(USER).document(uid).set(userData)
                    getUserData(uid)
                    inProgress.value = false
                }
            }
                .addOnFailureListener {
                    handleException(it, "Can't create user")
                    inProgress.value = false
                }
        }


    }

    private fun getUserData(uid: String) {
        inProgress.value = true
        db.collection(USER).document(uid).get()
            .addOnSuccessListener {
                val user = it.toObject<UserData>()
                userData.value = user
                inProgress.value = false
                refreshPosts()
                getPersonalizedFeed()
                getFollowers(user?.userID)
            }
            .addOnFailureListener { ext ->
                handleException(ext, "Can't retrieve user data")
            }
    }

    private fun handleException(exception: Exception? = null, customMassage: String = "") {
        exception?.printStackTrace()
        val errorMg = exception?.localizedMessage ?: ""
        val message = if (customMassage.isEmpty()) errorMg else "$customMassage: $errorMg"
        popUpNotification.value = Event(message)
    }

    fun updateProfileData(
        userName: String,
        name: String,
        bio: String,
    ) {
        createOrUpdateProfile(userName, name, bio)
    }

    private fun uploadImage(uri: Uri, onSuccess: (Uri) -> Unit) {
        inProgress.value = true
        val storageRef = storage.reference
        val uuid = UUID.randomUUID()
        val imageRef = storageRef.child("images/$uuid")
        val uploadTask = imageRef.putFile(uri)

        uploadTask.addOnSuccessListener {
            val result = it.metadata?.reference?.downloadUrl
            result?.addOnSuccessListener(onSuccess)
        }
            .addOnFailureListener { ex ->
                handleException(ex)
                inProgress.value = false
            }

    }


    fun uploadProfileImage(uri: Uri) {
        uploadImage(uri) {
            createOrUpdateProfile(imageUrl = it.toString())
            updatePostUserImageData(imageUrl = it.toString())
        }
    }


    fun onNewPost(
        uri: Uri,
        description: String,
        onPostSuccess: () -> Unit
    ) {
        uploadImage(uri) {
            onCreatePost(imageUrl = it, description = description, onPostSuccess = onPostSuccess)
        }
    }

    private fun onCreatePost(
        imageUrl: Uri?,
        description: String,
        onPostSuccess: () -> Unit
    ) {
        inProgress.value = true
        val currentUid = auth.currentUser?.uid
        val currentUserName = userData.value?.userName
        val currentUserImage = userData.value?.imageUrl
        if (currentUid != null) {
            val postUuid = UUID.randomUUID().toString()
            val fillerWords = listOf("the", "be", "to", "is", "of", "and", "or", "a", "in", "it")
            val searchTerms = description.split(" ", ".", ",", "?", "!", "#")
                .map { it.lowercase() }
                .filter { it.isNotEmpty() and !fillerWords.contains(it) }
            val post = PostData(
                postId = postUuid,
                userId = currentUid,
                userName = currentUserName,
                userImage = currentUserImage,
                postImage = imageUrl.toString(),
                postDescription = description,
                time = System.currentTimeMillis(),
                likes = listOf<String>(),
                searchTerms = searchTerms
            )

            db.collection(POSTS).document(postUuid).set(post)
                .addOnSuccessListener {
                    popUpNotification.value = Event("Post created successfully")
                    inProgress.value = false
                    refreshPosts()
                    onPostSuccess.invoke()
                }
                .addOnFailureListener { ex ->
                    handleException(ex, "Error creating post")
                    inProgress.value = false
                }


        } else {
            handleException(customMassage = "Error username unavailable. Unable to create post")
        }
    }

    private fun updatePostUserImageData(imageUrl: String?) {
        val currentUid = auth.currentUser?.uid
        db.collection(POSTS).whereEqualTo("userId", currentUid).get()
            .addOnSuccessListener { documents ->
                val posts = mutableStateOf<List<PostData>>(arrayListOf())
                convertPosts(documents, posts)
                val refs = arrayListOf<DocumentReference>()
                for (post in posts.value) {
                    post.postId?.let { id ->
                        refs.add(db.collection(POSTS).document(id))
                    }
                }
                if (refs.isNotEmpty()) {
                    db.runBatch { batch ->
                        for (ref in refs) {
                            batch.update(ref, "userImage", imageUrl)

                        }
                    }
                        .addOnSuccessListener {
                            refreshPosts()
                        }
                }
            }
            .addOnFailureListener { ex ->
                handleException(ex, "Can't update user image in posts")
            }
    }

    private fun refreshPosts() {
        val currentUid = auth.currentUser?.uid
        if (currentUid != null) {
            refreshPostProgress.value = true
            db.collection(POSTS).whereEqualTo("userId", currentUid).get()
                .addOnSuccessListener { documents ->
                    convertPosts(
                        documents = documents, outState = posts
                    )
                    refreshPostProgress.value = false
                }
                .addOnFailureListener { ex ->
                    handleException(ex, "Can't refresh posts")
                    refreshPostProgress.value = false
                }
        } else {
            handleException(customMassage = "Error username unavailable. Unable to create post")

        }
    }

    private fun convertPosts(documents: QuerySnapshot, outState: MutableState<List<PostData>>) {
        val newPosts = mutableListOf<PostData>()
        documents.forEach { doc ->
            val post = doc.toObject<PostData>()
            newPosts.add(post)
        }
        val sortedPosts = newPosts.sortedByDescending { it.time }
        outState.value = sortedPosts
    }


    fun searchPosts(searchTerm: String) {
        if (searchTerm.isNotEmpty()) {
            searchPostsProgress.value = true
            db.collection(POSTS).whereArrayContains("searchTerms", searchTerm.trim().lowercase())
                .get()
                .addOnSuccessListener {
                    convertPosts(it, searchedPosts)
                    searchPostsProgress.value = false
                }
                .addOnFailureListener { ex ->
                    handleException(ex, "can't search posts")
                    searchPostsProgress.value = false
                }
        }
    }

    fun onFollowClick(userId: String) {
        auth.currentUser?.uid?.let { currentUser ->
            val following = arrayListOf<String>()
            userData.value?.following?.let {
                following.addAll(it)
            }
            if (following.contains(userId)) {
                following.remove(userId)
            } else {
                following.add(userId)
            }
            db.collection(USER).document(currentUser).update("following", following)
                .addOnSuccessListener {
                    getUserData(currentUser)
                }
        }
    }

    private fun getPersonalizedFeed() {
        val following = userData.value?.following
        if (!following.isNullOrEmpty()) {
            postsFeedProgress.value = true
            db.collection(POSTS).whereIn("userId", following).get()
                .addOnSuccessListener {
                    convertPosts(documents = it, outState = postsFeed)
                    if (postsFeed.value.isEmpty()) {
                        getGeneralFeed()
                    } else {
                        postsFeedProgress.value = false
                    }
                }
                .addOnFailureListener { ex ->
                    handleException(ex, "can't get personalized feed")
                    postsFeedProgress.value = false
                }
        } else {
            getGeneralFeed()
        }
    }

    private fun getGeneralFeed() {
        postsFeedProgress.value = true
        val currentTime = System.currentTimeMillis()
        val difference = 24 * 60 * 60 * 1000 // day in millis
        db.collection(POSTS).whereGreaterThan("time", currentTime - difference)
            .get()
            .addOnSuccessListener {
                convertPosts(documents = it, outState = postsFeed)
                postsFeedProgress.value = false
            }
            .addOnFailureListener { ex ->
                handleException(ex, "can't get feed")
                postsFeedProgress.value = false
            }
    }

    fun onLikePost(postData: PostData) {
        auth.currentUser?.uid?.let { userId ->
            postData.likes?.let { likes ->
                val newLikes = arrayListOf<String>()
                if (likes.contains(userId)) {
                    newLikes.addAll(likes.filter { userId != it })
                } else {
                    newLikes.addAll(likes)
                    newLikes.add(userId)
                }
                postData.postId?.let { postId ->
                    db.collection(POSTS).document(postId).update("likes", newLikes)
                        .addOnSuccessListener {
                            postData.likes = newLikes
                        }
                        .addOnFailureListener { ex ->
                            handleException(ex, "Unable to like post")
                        }
                }
            }
        }
    }

    fun createComment(postId: String, text: String) {
        userData.value?.userName?.let { userName ->
            val commentId = UUID.randomUUID().toString()
            val comment = CommentData(
                commentId = commentId,
                postId = postId,
                userName = userName,
                text = text,
                timesTamp = System.currentTimeMillis()
            )
            db.collection(COMMENTS).document(commentId).set(comment)
                .addOnSuccessListener {
                    getComments(postId = postId)
                }
                .addOnFailureListener { ex ->
                    handleException(ex, "Can't create comment")
                }
        }
    }


    fun getComments(postId: String?) {
        commentsProgress.value = true
        db.collection(COMMENTS).whereEqualTo("postId", postId).get()
            .addOnSuccessListener { documents ->
                val newComments = mutableListOf<CommentData>()
                documents.forEach { doc ->
                    val comment = doc.toObject<CommentData>()
                    newComments.add(comment)
                }
                val sortedComments = newComments.sortedByDescending { it.timesTamp }
                comments.value = sortedComments
                commentsProgress.value = false
            }.addOnFailureListener { ex ->
                handleException(ex, "Can't retrieve comments")
                commentsProgress.value = false
            }
    }

    private fun getFollowers(uid: String?) {
        db.collection(USER).whereArrayContains("following", uid ?: "").get()
            .addOnSuccessListener { document ->
                followers.value = document.size()
            }
    }

}