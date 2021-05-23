package si.dragonhack.petpal.data.utils

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore

class Images {
    companion object {
        fun getPath(uri: Uri?, contentResolver: ContentResolver): String? {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(uri!!, projection, null, null, null) ?: return null
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            val s = cursor.getString(column_index)
            cursor.close()
            return s
        }

    }
}