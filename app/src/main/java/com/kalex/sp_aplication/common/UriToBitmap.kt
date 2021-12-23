package com.kalex.sp_aplication.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.io.ByteArrayOutputStream
import java.io.IOException

 fun getCapturedImage(selectedPhotoUri: Uri): Bitmap? {
     var bitMin: Bitmap? = null
     if(selectedPhotoUri !== Uri.parse("file://dev/null")){

         val bit = BitmapFactory.decodeFile(selectedPhotoUri.path)

         bitMin = Bitmap.createScaledBitmap(bit,190,197,true)

         //val bytearrayoutputstream = ByteArrayOutputStream()
         //val compress = bit.compress(Bitmap.CompressFormat.JPEG,40,bytearrayoutputstream)

     }
     //devuelve un bitmap

    return bitMin
}

fun getGaleryImage(selectedPhotoUri: Uri, context: Context): Bitmap?{
    var bitMin: Bitmap? = null
     var bitmap: Bitmap? = null
     if (Build.VERSION.SDK_INT >= 29) {
         val source = ImageDecoder.createSource(
             context.contentResolver,
             selectedPhotoUri
         )
         try {
             bitmap = ImageDecoder.decodeBitmap(source)
         } catch (e: IOException) {
             e.printStackTrace()
         }
     } else {
         try {
             bitmap = MediaStore.Images.Media.getBitmap(
                 context.contentResolver, selectedPhotoUri
             )
         } catch (e: IOException) {
             e.printStackTrace()
         }
     }
    if(selectedPhotoUri !== Uri.parse("file://dev/null")){
        bitMin = bitmap?.let { Bitmap.createScaledBitmap(it,190,197,true) }
    }


    return bitMin
}

