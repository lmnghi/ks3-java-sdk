package com.ksyun.ks3.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年10月22日 下午2:45:20
 * 
 * @description 
 **/
public class Mimetypes {
	 private static final Log log = LogFactory.getLog(Mimetypes.class);

	    /** The default XML mimetype: application/xml */
	    public static final String MIMETYPE_XML = "application/xml";

	    /** The default HTML mimetype: text/html */
	    public static final String MIMETYPE_HTML = "text/html";

	    /** The default binary mimetype: application/octet-stream */
	    public static final String MIMETYPE_OCTET_STREAM = "application/octet-stream";

	    /** The default gzip mimetype: application/x-gzip */
	    public static final String MIMETYPE_GZIP = "application/x-gzip";

	    private static Mimetypes mimetypes = null;

	    private HashMap<String, String> extensionToMimetypeMap = new HashMap<String, String>();

	    private Mimetypes() {}
	    public synchronized static Mimetypes getInstance() {
	        if (mimetypes != null) return mimetypes;

	        mimetypes = new Mimetypes();
	        InputStream is = mimetypes.getClass().getResourceAsStream("/mime.types");
	        if (is != null) {
	            if (log.isDebugEnabled()) {
	                log.debug("Loading mime types from file in the classpath: mime.types");
	            }
	            try {
	                mimetypes.loadAndReplaceMimetypes(is);
	            } catch (IOException e) {
	                if (log.isErrorEnabled()) {
	                    log.error("Failed to load mime types from file in the classpath: mime.types", e);
	                }
	            } finally {
	                try { is.close(); } catch (IOException ex) { log.debug("", ex); }
	            }
	        } else {
	            if (log.isWarnEnabled()) {
	                log.warn("Unable to find 'mime.types' file in classpath");
	            }
	        }
	        return mimetypes;
	    }
	    public void loadAndReplaceMimetypes(InputStream is) throws IOException {
	        BufferedReader br = new BufferedReader(new InputStreamReader(is));
	        String line =  null;

	        while ((line = br.readLine()) != null) {
	            line = line.trim();

	            if (line.startsWith("#") || line.length() == 0) {
	                // Ignore comments and empty lines.
	            } else {
	                StringTokenizer st = new StringTokenizer(line, " \t");
	                if (st.countTokens() > 1) {
	                    String mimetype = st.nextToken();
	                    while (st.hasMoreTokens()) {
	                        String extension = st.nextToken();
	                        extensionToMimetypeMap.put(extension.toLowerCase(), mimetype);
	                        if (log.isDebugEnabled()) {
	                            log.debug("Setting mime type for extension '" + extension.toLowerCase() + "' to '" + mimetype + "'");
	                        }
	                    }
	                } else {
	                    if (log.isDebugEnabled()) {
	                        log.debug("Ignoring mimetype with no associated file extensions: '" + line + "'");
	                    }
	                }
	            }
	        }
	    }
	    public String getMimetype(String fileName) {
	        int lastPeriodIndex = fileName.lastIndexOf(".");
	        if (lastPeriodIndex > 0 && lastPeriodIndex + 1 < fileName.length()) {
	            String ext = fileName.substring(lastPeriodIndex + 1).toLowerCase();
	            if (extensionToMimetypeMap.keySet().contains(ext)) {
	                String mimetype = (String) extensionToMimetypeMap.get(ext);
	                if (log.isDebugEnabled()) {
	                    log.debug("Recognised extension '" + ext + "', mimetype is: '" + mimetype + "'");
	                }
	                return mimetype;
	            } else {
	                if (log.isDebugEnabled()) {
	                    log.debug("Extension '" + ext + "' is unrecognized in mime type listing"
	                    + ", using default mime type: '" + MIMETYPE_OCTET_STREAM + "'");
	                }
	            }
	        } else {
	            if (log.isDebugEnabled()) {
	                log.debug("File name has no extension, mime type cannot be recognised for: " + fileName);
	            }
	        }
	        return MIMETYPE_OCTET_STREAM;
	    }
	    public String getMimetype(File file) {
	       return getMimetype(file.getName());
	    }
}
