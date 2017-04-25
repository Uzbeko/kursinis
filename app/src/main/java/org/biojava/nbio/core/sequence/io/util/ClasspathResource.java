/*
 *                    BioJava development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *      http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright for this code is held jointly by the individual
 * authors.  These should be listed in @author doc comments.
 *
 * For more information on the BioJava project and its aims,
 * or to join the biojava-l mailing list, visit the home page
 * at:
 *
 *      http://www.biojava.org/
 *
 * Created on 01-21-2010
 */
package org.biojava.nbio.core.sequence.io.util;

import org.biojava.MyApplication;
import org.biojava.R;
import org.biojava.nbio.core.exceptions.ParserException;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.zip.GZIPInputStream;

import static org.biojava.nbio.core.sequence.io.util.IOUtils.close;
import static org.biojava.nbio.core.sequence.io.util.IOUtils.copy;

/**
 * This object represents a classpath resource on the local system. It allows
 * you to specify a location and then extract the inputstream, reader or
 * lines of the resource. We also support GZiped files (so long as the resource
 * ends with a .gz) and pre-caching of the data so we read only once from
 * the classpath and close that link down. This is useful if you want to keep
 * IO handles down but not very useful if the file is very large.
 *
 * @author ayates
 *
 */
public class ClasspathResource {

	private final String location;
	private final boolean preCache;
	private final Boolean isGzip;

	/**
	 * Basic constructor only allowing you to specify where to find the file.
	 *
	 * @param location Specified as <i>my/classpath/loc.txt</i>
	 */
	public ClasspathResource(String location) {
		this(location, false);
	}

	/**
	 * Advanced constructor which allows you to optionally pre-cache the
	 * data
	 *
	 * @param location Specified as <i>my/classpath/loc.txt</i>
	 * @param preCache If set to true will cause the data to be copied
	 * to an in memory byte array and then an InputStream will be wrapped around
	 * that.
	 */
	public ClasspathResource(String location, boolean preCache) {
		this.location = location;
		this.preCache = preCache;
		this.isGzip = null;
	}

	/**
	 * Advanced constructor which lets you set the preCache variable and to
	 * force the type of file we are decompressing. If this constructor is
	 * used we trust your call as to the file's compression status.
	 *
	 * @param location Specified as <i>my/classpath/loc.txt</i>
	 * @param preCache If set to true will cause the data to be copied
	 * to an in memory byte array and then an InputStream will be wrapped around
	 * that.
	 * @param isGzip Set to true or false if the file is gziped.
	 */
	public ClasspathResource(String location, boolean preCache, boolean isGzip) {
		this.location = location;
		this.preCache = preCache;
		this.isGzip = isGzip;
	}

	/**
	 * Returns the InputStream instance of this classpath resource
	 */
	public InputStream getInputStream() {
		return createClasspathInputStream();
	}

	/**
	 * Returns the reader representation of this classpath resource
	 */
	public BufferedReader getBufferedReader() {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

	/**
	 * Returns this resource as a list of Strings
	 */
	public List<String> getList() {
		return IOUtils.getList(getBufferedReader());
	}

	private InputStream createClasspathInputStream() {
		final InputStream is;
		//pakeiciau source direktorija, nes androidas turi savo specifine!!!!!!!!!!!!!!!!!!!!!!!!
		final InputStream classpathIs = getClass().getClassLoader().getResourceAsStream(location);//senoji direktorija

//-------------test-------------------------
////		InputStream classpathIs = MyApplication.getAppContext().getResources().openRawResource(R.raw.iupac);
////			System.out.print("asdf");
//		String resource = "/home/edvinas/AndroidStudioProjects/Biojava/app/build/intermediates/sourceFolderJavaResources/test/debug/core/search/io/blast/small-blastreport.blastxml";
//
//		URL resourceURL = getClass().getResource(resource);
//		File file = new File(resourceURL.getFile());
//
//
//		InputStream inStream = ClasspathResource.class.getResourceAsStream("/iupac.txt");
//
//		String url = "org/biojava/nbio/core/sequence/iupac.txt";
//
//		final InputStream classpathIs = this.getClass().getResourceAsStream(url);//nauja lokacija
////		final InputStream classpathIs = this.getClass().getClassLoader().getResourceAsStream(location);//nauja lokacija
//		InputStream classpathIs = MyApplication.getAppContext().getResources().openRawResource(R.raw.iupac);
//----------------test end----------------------
		//Todo iharkodinau nes nezinau kaip paimti faila (sutvarkyti sita)
//		String str = "UNIVERSAL=1\n" +
//				"AAs    = FFLLSSSSYY**CC*WLLLLPPPPHHQQRRRRIIIMTTTTNNKKSSRRVVVVAAAADDEEGGGG\n" +
//				"Starts = ---M---------------M---------------M----------------------------\n" +
//				"Base1  = TTTTTTTTTTTTTTTTCCCCCCCCCCCCCCCCAAAAAAAAAAAAAAAAGGGGGGGGGGGGGGGG\n" +
//				"Base2  = TTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGG\n" +
//				"Base3  = TCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAG\n" +
//				"//\n" +
//				"VERTEBRATE_MITOCHONDRIAL=2\n" +
//				"AAs    = FFLLSSSSYY**CCWWLLLLPPPPHHQQRRRRIIMMTTTTNNKKSS**VVVVAAAADDEEGGGG\n" +
//				"Starts = --------------------------------MMMM---------------M------------\n" +
//				"Base1  = TTTTTTTTTTTTTTTTCCCCCCCCCCCCCCCCAAAAAAAAAAAAAAAAGGGGGGGGGGGGGGGG\n" +
//				"Base2  = TTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGG\n" +
//				"Base3  = TCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAG\n" +
//				"//\n" +
//				"YEAST_MITOCHONDRIAL=3\n" +
//				"AAs    = FFLLSSSSYY**CCWWTTTTPPPPHHQQRRRRIIMMTTTTNNKKSSRRVVVVAAAADDEEGGGG\n" +
//				"Starts = ----------------------------------MM----------------------------\n" +
//				"Base1  = TTTTTTTTTTTTTTTTCCCCCCCCCCCCCCCCAAAAAAAAAAAAAAAAGGGGGGGGGGGGGGGG\n" +
//				"Base2  = TTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGG\n" +
//				"Base3  = TCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAG\n" +
//				"//\n" +
//				"MOLD_MITOCHONDRIAL=4\n" +
//				"AAs    = FFLLSSSSYY**CCWWLLLLPPPPHHQQRRRRIIIMTTTTNNKKSSRRVVVVAAAADDEEGGGG\n" +
//				"Starts = --MM---------------M------------MMMM---------------M------------\n" +
//				"Base1  = TTTTTTTTTTTTTTTTCCCCCCCCCCCCCCCCAAAAAAAAAAAAAAAAGGGGGGGGGGGGGGGG\n" +
//				"Base2  = TTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGG\n" +
//				"Base3  = TCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAG\n" +
//				"//\n" +
//				"INVERTEBRATE_MITOCHONDRIAL=5\n" +
//				"AAs    = FFLLSSSSYY**CCWWLLLLPPPPHHQQRRRRIIMMTTTTNNKKSSSSVVVVAAAADDEEGGGG\n" +
//				"Starts = ---M----------------------------MMMM---------------M------------\n" +
//				"Base1  = TTTTTTTTTTTTTTTTCCCCCCCCCCCCCCCCAAAAAAAAAAAAAAAAGGGGGGGGGGGGGGGG\n" +
//				"Base2  = TTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGG\n" +
//				"Base3  = TCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAG\n" +
//				"//\n" +
//				"CILIATE_NUCLEAR=6\n" +
//				"AAs    = FFLLSSSSYYQQCC*WLLLLPPPPHHQQRRRRIIIMTTTTNNKKSSRRVVVVAAAADDEEGGGG\n" +
//				"Starts = -----------------------------------M----------------------------\n" +
//				"Base1  = TTTTTTTTTTTTTTTTCCCCCCCCCCCCCCCCAAAAAAAAAAAAAAAAGGGGGGGGGGGGGGGG\n" +
//				"Base2  = TTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGG\n" +
//				"Base3  = TCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAG\n" +
//				"//\n" +
//				"ECHINODERM_MITOCHONDRIAL=9\n" +
//				"AAs    = FFLLSSSSYY**CCWWLLLLPPPPHHQQRRRRIIIMTTTTNNNKSSSSVVVVAAAADDEEGGGG\n" +
//				"Starts = -----------------------------------M---------------M------------\n" +
//				"Base1  = TTTTTTTTTTTTTTTTCCCCCCCCCCCCCCCCAAAAAAAAAAAAAAAAGGGGGGGGGGGGGGGG\n" +
//				"Base2  = TTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGG\n" +
//				"Base3  = TCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAG\n" +
//				"//\n" +
//				"EUPLOTID_NUCLEAR=10\n" +
//				"AAs    = FFLLSSSSYY**CCCWLLLLPPPPHHQQRRRRIIIMTTTTNNKKSSRRVVVVAAAADDEEGGGG\n" +
//				"Starts = -----------------------------------M----------------------------\n" +
//				"Base1  = TTTTTTTTTTTTTTTTCCCCCCCCCCCCCCCCAAAAAAAAAAAAAAAAGGGGGGGGGGGGGGGG\n" +
//				"Base2  = TTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGG\n" +
//				"Base3  = TCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAG\n" +
//				"//\n" +
//				"BACTERIAL=11\n" +
//				"AAs    = FFLLSSSSYY**CC*WLLLLPPPPHHQQRRRRIIIMTTTTNNKKSSRRVVVVAAAADDEEGGGG\n" +
//				"Starts = ---M---------------M------------MMMM---------------M------------\n" +
//				"Base1  = TTTTTTTTTTTTTTTTCCCCCCCCCCCCCCCCAAAAAAAAAAAAAAAAGGGGGGGGGGGGGGGG\n" +
//				"Base2  = TTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGG\n" +
//				"Base3  = TCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAG\n" +
//				"//\n" +
//				"ALTERNATIVE_YEAST_NUCLEAR=12\n" +
//				"AAs    = FFLLSSSSYY**CC*WLLLSPPPPHHQQRRRRIIIMTTTTNNKKSSRRVVVVAAAADDEEGGGG\n" +
//				"Starts = -------------------M---------------M----------------------------\n" +
//				"Base1  = TTTTTTTTTTTTTTTTCCCCCCCCCCCCCCCCAAAAAAAAAAAAAAAAGGGGGGGGGGGGGGGG\n" +
//				"Base2  = TTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGG\n" +
//				"Base3  = TCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAG\n" +
//				"//\n" +
//				"ASCIDIAN_MITOCHONDRIAL=13\n" +
//				"AAs    = FFLLSSSSYY**CCWWLLLLPPPPHHQQRRRRIIMMTTTTNNKKSSGGVVVVAAAADDEEGGGG\n" +
//				"Starts = ---M------------------------------MM---------------M------------\n" +
//				"Base1  = TTTTTTTTTTTTTTTTCCCCCCCCCCCCCCCCAAAAAAAAAAAAAAAAGGGGGGGGGGGGGGGG\n" +
//				"Base2  = TTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGG\n" +
//				"Base3  = TCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAG\n" +
//				"//\n" +
//				"FLATWORM_MITOCHONDRIAL=14\n" +
//				"AAs    = FFLLSSSSYYY*CCWWLLLLPPPPHHQQRRRRIIIMTTTTNNNKSSSSVVVVAAAADDEEGGGG\n" +
//				"Starts = -----------------------------------M----------------------------\n" +
//				"Base1  = TTTTTTTTTTTTTTTTCCCCCCCCCCCCCCCCAAAAAAAAAAAAAAAAGGGGGGGGGGGGGGGG\n" +
//				"Base2  = TTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGG\n" +
//				"Base3  = TCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAG\n" +
//				"//\n" +
//				"BLEPHARISMA_MACRONUCLEAR=15\n" +
//				"AAs    = FFLLSSSSYY*QCC*WLLLLPPPPHHQQRRRRIIIMTTTTNNKKSSRRVVVVAAAADDEEGGGG\n" +
//				"Starts = -----------------------------------M----------------------------\n" +
//				"Base1  = TTTTTTTTTTTTTTTTCCCCCCCCCCCCCCCCAAAAAAAAAAAAAAAAGGGGGGGGGGGGGGGG\n" +
//				"Base2  = TTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGG\n" +
//				"Base3  = TCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAG\n" +
//				"//\n" +
//				"CHLOROPHYCEAN_MITOCHONDRIAL=16\n" +
//				"AAs    = FFLLSSSSYY*LCC*WLLLLPPPPHHQQRRRRIIIMTTTTNNKKSSRRVVVVAAAADDEEGGGG\n" +
//				"Starts = -----------------------------------M----------------------------\n" +
//				"Base1  = TTTTTTTTTTTTTTTTCCCCCCCCCCCCCCCCAAAAAAAAAAAAAAAAGGGGGGGGGGGGGGGG\n" +
//				"Base2  = TTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGG\n" +
//				"Base3  = TCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAG\n" +
//				"//\n" +
//				"TREMATODE_MITOCHONDRIAL=21\n" +
//				"AAs    = FFLLSSSSYY**CCWWLLLLPPPPHHQQRRRRIIMMTTTTNNNKSSSSVVVVAAAADDEEGGGG\n" +
//				"Starts = -----------------------------------M---------------M------------\n" +
//				"Base1  = TTTTTTTTTTTTTTTTCCCCCCCCCCCCCCCCAAAAAAAAAAAAAAAAGGGGGGGGGGGGGGGG\n" +
//				"Base2  = TTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGG\n" +
//				"Base3  = TCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAG\n" +
//				"//\n" +
//				"SCENEDESMUS_MITOCHONDRIAL=22\n" +
//				"AAs    = FFLLSS*SYY*LCC*WLLLLPPPPHHQQRRRRIIIMTTTTNNKKSSRRVVVVAAAADDEEGGGG\n" +
//				"Starts = -----------------------------------M----------------------------\n" +
//				"Base1  = TTTTTTTTTTTTTTTTCCCCCCCCCCCCCCCCAAAAAAAAAAAAAAAAGGGGGGGGGGGGGGGG\n" +
//				"Base2  = TTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGG\n" +
//				"Base3  = TCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAG\n" +
//				"//\n" +
//				"THRAUSTOCHYTRIUM_MITOCHONDRIAL=23\n" +
//				"AAs    = FF*LSSSSYY**CC*WLLLLPPPPHHQQRRRRIIIMTTTTNNKKSSRRVVVVAAAADDEEGGGG\n" +
//				"Starts = --------------------------------M--M---------------M------------\n" +
//				"Base1  = TTTTTTTTTTTTTTTTCCCCCCCCCCCCCCCCAAAAAAAAAAAAAAAAGGGGGGGGGGGGGGGG\n" +
//				"Base2  = TTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGG\n" +
//				"Base3  = TCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAG\n" +
//				"//";
//		InputStream classpathIs = new ByteArrayInputStream(str.getBytes());

		if(classpathIs == null) {
			throw new IllegalArgumentException("Location "+
					location+" resulted in a null InputStream");
		}
		if(preCache) {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			try {
				copy(classpathIs, os);
			} catch (IOException e) {
				throw new ParserException("Cannot copy classpath InputStream", e);
			}
			finally {
				close(classpathIs);
			}
			is = new ByteArrayInputStream(os.toByteArray());
		}
		else {
			is = classpathIs;
		}

		if(isGzip()) {
			try {
				return new GZIPInputStream(is);
			}
			catch (IOException e) {
				throw new ParserException("Cannot open stream as a GZIP stream", e);
			}
		}

		return is;
	}

	/**
	 * Returns true if the location given ends with a .gz extension. No magic
	 * number investigation is done.
	 */
	private boolean isGzip() {
		if(isGzip != null) {
			return isGzip;
		}
		else {
			return this.location.endsWith(".gz");
		}
	}
}
