import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson

data class PostData(
    val postId: String? = null,
    val userId: String? = null,
    val userName: String? = null,
    val userImage: String? = null,
    val postImage: String? = null,
    val postDescription: String? = null,
    val time: Long? = null,
    var likes: List<String>? = null,
    val searchTerms: List<String>? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.createStringArrayList(),
        parcel.createStringArrayList()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(postId)
        parcel.writeString(userId)
        parcel.writeString(userName)
        parcel.writeString(userImage)
        parcel.writeString(postImage)
        parcel.writeString(postDescription)
        parcel.writeValue(time)
        parcel.writeStringList(likes)
        parcel.writeStringList(searchTerms)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun toJson(): String {
        return Gson().toJson(this)
    }
    companion object CREATOR : Parcelable.Creator<PostData> {
        override fun createFromParcel(parcel: Parcel): PostData {
            return PostData(parcel)
        }

        override fun newArray(size: Int): Array<PostData?> {
            return arrayOfNulls(size)
        }

        fun fromJson(json: String): PostData {
            return Gson().fromJson(json, PostData::class.java)
        }
    }
}