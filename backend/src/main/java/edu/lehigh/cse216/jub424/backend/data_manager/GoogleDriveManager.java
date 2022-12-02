package edu.lehigh.cse216.jub424.backend.data_manager;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.Drive.Files.Get;
import com.google.api.services.drive.model.File;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.GeneralSecurityException;

import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import edu.lehigh.cse216.jub424.backend.data_structure.*;

/**
 * ResourceManager contains a function for making requests to the Drive API
 * 
 * @author Ala Chua
 * @version 1.0.0
 * @since 2022-11-16
 */
/* class to demonstarte use of Drive files list API */
// public class GoogleDriveManager {

// public void GoogleDrive(String... args) throws IOException,
// GeneralSecurityException {
// // Build a new authorized API client service.
// final NetHttpTransport HTTP_TRANSPORT =
// GoogleNetHttpTransport.newTrustedTransport();
// Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY,
// getCredentials(HTTP_TRANSPORT))
// .setApplicationName(APPLICATION_NAME).build();

// // Print the names and IDs for up to 10 files.
// FileList result =
// service.files().list().setPageSize(10).setFields("nextPageToken, files(id,
// name)").execute();
// List<File> files = result.getFiles();
// if (files == null || files.isEmpty()) {
// System.out.println("No files found.");
// } else {
// System.out.println("Files:");
// for (File file : files) {
// System.out.printf("%s (%s)\n", file.getName(), file.getId());
// }
// }
// }
public class GoogleDriveManager {
  /**
   * Application name.
   */
  private static final String APPLICATION_NAME = "Google Drive API Java";

  /**
   * Global instance of the scopes required by this quickstart.
   * If modifying these scopes, delete your previously saved tokens/ folder.
   */
  private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_FILE);
  // Not getting credentials from file, getting credentials from Heroku config
  // vars
  // private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

  // /**
  // * Creates an authorized Credential object.
  // *
  // * @param HTTP_TRANSPORT The network HTTP Transport.
  // * @return An authorized Credential object.
  // * @throws IOException If the credentials.json file cannot be found.
  // */
  private static GoogleCredentials getCredentials() throws Exception {
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    // Load credentials
    Map<String, String> env = System.getenv();
    // if (env.get("CREDENTIALS_KEY") == null) {
    // throw new Exception("Cannot get credentials key");
    // }
    String filepath = "./src/main/java/resources/credentials.json";
    // Convert to input stream
    // InputStream in =
    // GoogleDriveManager.class.getClass().getResourceAsStream(env.get("CREDENTIALS_KEY"));
    // InputStream in =
    // GoogleDriveManager.class.getClass().getResourceAsStream(filepath);
    InputStream in = new FileInputStream(filepath);
    if (in == null) {
      throw new Exception("Cannot load credentials key" + filepath);
    }
    // Get GoogleCredentials object
    GoogleCredentials credential = GoogleCredentials.fromStream(in).createScoped(Arrays.asList(DriveScopes.DRIVE_FILE));
    if (credential == null) {
      throw new Exception("Could not create credentials object " + credential);
    }
    // System.out.println("Credential: " + credential);
    // returns an authorized Credential object.
    return credential;
  }

  /**
   * quickStart Method to test getting credentials
   * 
   * @throws Exception
   */
  public static void quickStart() throws Exception {
    // Build a new authorized API client service.
    GoogleCredentials credentials = getCredentials();
    // throw new Exception("credentials " + credentials);
    // Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY,
    // (HttpRequestInitializer) getCredentials(HTTP_TRANSPORT))
    // .setApplicationName(APPLICATION_NAME)
    // .build();
    // // Print the names and IDs for up to 10 files.
    // FileList result = service.files().list()
    // .setPageSize(10)
    // .setFields("nextPageToken, files(id, name)")
    // .execute();
    // List<File> files = result.getFiles();
    // if (files == null || files.isEmpty()) {
    // System.out.println("No files found.");
    // } else {
    // System.out.println("Files:");
    // for (File file : files) {
    // System.out.printf("%s (%s)\n", file.getName(), file.getId());
    // }
    // }
  }

  /**
   * Upload new file to the Google Drive service account
   * 
   * @param res The resource object
   *
   * @return Inserted file metadata if successful, {@code null} otherwise.
   * @throws Exception
   */
  public static String uploadBasic(String base64, String MIME, String filename, int id, int com_id) throws Exception {
    // 1JjxOgz_GPITHz7dfStaxPp2QelQFOAWP
    // final NetHttpTransport HTTP_TRANSPORT =
    // GoogleNetHttpTransport.newTrustedTransport();
    // Try and catch block for loading credentials
    // Load pre-authorized user credentials from the environment.
    GoogleCredentials credentials = getCredentials();
    HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);
    // Build a new authorized API client service.
    Drive service = new Drive.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance(), requestInitializer)
        .setApplicationName(APPLICATION_NAME).build();
    // Upload file on drive
    // Create file object and initialize with file information
    // Decode base64
    byte[] decodedBytes = Base64.getDecoder().decode(base64);
    String decodedBase64 = new String(decodedBytes);
    // throw new Exception("decoded: " + decodedBytes);
    File fileMetadata = new File();
    fileMetadata.setName(filename);
    // File's content.
    List<String> parents = Arrays.asList("1JjxOgz_GPITHz7dfStaxPp2QelQFOAWP");
    fileMetadata.setParents(parents);
    AbstractInputStreamContent uploadStreamContent = new ByteArrayContent(MIME, decodedBytes);
    File file = null;
    file = service.files().create(fileMetadata, uploadStreamContent)
        .setFields("id, webContentLink, webViewLink, parents").execute();
    File getFile = service.files().get(file.getId()).setFields("webViewLink").execute();
    String link = getFile.getWebViewLink();
    return link;
    // String base64Content = getBase64(base64);
    // URI uri = new URI(base64);
    // if (uri.getPath() == null) {
    // throw new Exception("URI path not found.");
    // }
    // java.io.File filePath = new java.io.File(uri.getPath());
    // Specify media type and file-path for file.
    // FileContent mediaContent = new FileContent(MIME, new
    // java.io.File(uri.getPath()));
    // throw new Exception("File content " + mediaContent);
  }

}