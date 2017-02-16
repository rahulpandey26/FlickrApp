package com.robosoft.flickrapp.utills;

/**
 * Created by rahul on 26/5/16.
 */
public class Constants {

    public interface ServerValues {
        String ID = "id";
        String TITLE = "title";
        String SECRET_KEY = "secret";
        String SERVER_KEY = "server";
        String FARM_KEY = "farm";
        String JSON_OBJECT= "photos";
        String JSON_ARRAY = "photo";
    }

    public interface ImageUrlValues {
        String FARM_ID = "{farm-id}";
        String SERVER_ID = "{server-id}";
        String ID_KEY = "{id}";
        String SECERT = "{secret}";
    }

    public interface BundleKey {
        String BUNDLE_KEY_REFERENCE = "refrence";
        String BUNDLE_KEY_IMAGE = "image";
        String BUNDLE_REFERENCE_VALUE_HOME = "HomeImage";
        String BUNDLE_REFERENCE_VALUE_FAV = "FavoriteImage";
        String BUNDLE_REFERENCE_VALUE_DOWNLOAD = "DownloadedImage";
    }

    public interface SdFolder {
        String DOWNLOADED_IMAGE_FOLDER = "/saved_images";
        String IMAGE_EXTENSION = ".jpg";
    }

    public interface DatabaseCommand {
        String UPGRADE_QUERY = "DROP TABLE IF EXISTS";
        String DELETE_QUERY = "delete from ";
    }

    public interface SwitchCaseValue {
        String HOME_IMAGE = "HomeImage";
        String DOWNLOADED_IMAGE = "DownloadedImage";
        String FAVORITE_IMAGE = "FavoriteImage";
    }
    public  interface FbSharing {
        String PERMISSION = "publish_actions";
        String FB_SHARE_KEY = "fbShareUrl";
    }
    public interface FragmentTransactionKey {
        String TAG = "tag_key";
    }

   /* public  static enum FragmentType {
        HOME , FAVORITE , DOWNLOAD
    }*/
}
