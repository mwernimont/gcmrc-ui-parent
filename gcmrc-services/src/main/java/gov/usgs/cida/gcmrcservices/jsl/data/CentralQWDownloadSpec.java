package gov.usgs.cida.gcmrcservices.jsl.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dmsibley
 */
public class CentralQWDownloadSpec extends QWDownloadSpec {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(CentralQWDownloadSpec.class);

	@Override
	public String getTimezoneDisplay() {
		return "CST";
	}
	
	@Override
	public String getTimezoneSql() {
		return " + 1/24 ";
	}
}
