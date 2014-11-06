package gov.usgs.cida.gcmrcservices.jsl.data;

import gov.usgs.cida.gcmrcservices.column.ColumnMetadata;
import static gov.usgs.cida.gcmrcservices.jsl.data.ParameterSpec.C_TSM_DT;
import gov.usgs.cida.gcmrcservices.nude.Endpoint;
import gov.usgs.webservices.jdbc.spec.mapping.ColumnMapping;
import gov.usgs.webservices.jdbc.spec.mapping.SearchMapping;
import gov.usgs.webservices.jdbc.spec.mapping.WhereClauseType;
import gov.usgs.webservices.jdbc.util.CleaningOption;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BedMaterialSpec extends DataSpec {

	private static final long serialVersionUID = 2263816089456993501L;
	private static final Logger log = LoggerFactory.getLogger(BedMaterialSpec.class);

	public BedMaterialSpec(String stationName, ParameterCode parameterCode, SpecOptions options) {
		super(stationName, parameterCode, options);
	}

	@Override
	public ColumnMapping[] setupColumnMap() {
		ColumnMapping[] result = null;
		
		if (null != this.stationName && null != this.parameterCode) {
			result = new ColumnMapping[] {
				new ColumnMapping(ParameterSpec.C_TSM_DT, ParameterSpec.S_TSM_DT),
				new ColumnMapping(C_SITE_NAME, S_SITE_NAME),
				new ColumnMapping(ColumnMetadata.createColumnName(this.stationName, this.parameterCode) + C_BED_VALUE, S_BED_VALUE),
				new ColumnMapping(ColumnMetadata.createColumnName(this.stationName, this.parameterCode) + C_SAMPLE_MASS, S_SAMPLE_MASS),
				new ColumnMapping(ColumnMetadata.createColumnName(this.stationName, this.parameterCode) + C_SAMPLE_SET, S_SAMPLE_SET)
			};
		} else {
			log.trace("setupColumnMap stationName=" + this.stationName + " parameterCode=" + this.parameterCode);
		}
		
		return result;
	}
	
	@Override
	public SearchMapping[] setupSearchMap() {
		SearchMapping[] result = new SearchMapping[] {
			new SearchMapping(Endpoint.BEGIN_KEYWORD, C_TSM_DT, null, WhereClauseType.special, CleaningOption.none, "TO_DATE(" + FIELD_NAME_KEY + ", 'YYYY-MM-DD\"T\"HH24:MI:SS') >= TO_DATE(" + USER_VALUE_KEY + ", 'YYYY-MM-DD\"T\"HH24:MI:SS')", null),
			new SearchMapping(Endpoint.END_KEYWORD, C_TSM_DT, null, WhereClauseType.special, CleaningOption.none, "TO_DATE(" + FIELD_NAME_KEY + ", 'YYYY-MM-DD\"T\"HH24:MI:SS') <= TO_DATE(" + USER_VALUE_KEY + ", 'YYYY-MM-DD\"T\"HH24:MI:SS')", null)
		};
		
		return result;
	}
	
	@Override
	public String setupTableName() {
		StringBuilder result = new StringBuilder();
		
		result.append("(");
		result.append("  SELECT TO_CHAR(BED_MEAS_DT, 'YYYY-MM-DD\"T\"HH24:MI:SS') AS TSM_DT,");
		result.append("    NVL(S.NWIS_SITE_NO, S.SHORT_NAME) AS SITE_NAME,");
		result.append("    VAL AS ").append(ColumnMetadata.createColumnName(this.stationName, this.parameterCode)).append(C_BED_VALUE).append(",");
		result.append("    SAMPLE_MASS AS ").append(ColumnMetadata.createColumnName(this.stationName, this.parameterCode)).append(C_SAMPLE_MASS).append(",");
		result.append("    SAMPLE_SET AS ").append(ColumnMetadata.createColumnName(this.stationName, this.parameterCode)).append(C_SAMPLE_SET).append("");
		result.append("  FROM");
		result.append("    (SELECT BED_MEAS_DT,");
		result.append("      SITE_ID,");
		result.append("      MAX(");
		result.append("      CASE");
		result.append("        WHEN BM.GROUP_ID = 15");
		result.append("        THEN BM.BED_VALUE");
		result.append("      END) VAL,");
		result.append("      MAX(");
		result.append("      CASE");
		result.append("        WHEN BM.GROUP_ID = 14");
		result.append("        THEN BM.BED_VALUE");
		result.append("      END) SAMPLE_MASS,");
		result.append("      BM.SAMPLE_SET");
		result.append("    FROM BED_MATERIAL BM");
		result.append("    WHERE BM.GROUP_ID IN (14, 15)");
		result.append("    GROUP BY BED_MEAS_DT,");
		result.append("      SITE_ID,");
		result.append("      BM.SAMPLE_SET");
		result.append("    ) T_A_BED,");
		result.append("    SITE_STAR S");
		result.append("  WHERE T_A_BED.SITE_ID = S.SITE_ID");
		result.append(") T_A_BED_SEDIMENT");
		
		return result.toString();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append("TODO I'm a " + this.stationName + " station " + this.parameterCode.toString() + " Bed Material Spec!")
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) { return false; }
		if (obj == this) { return true; }
		if (obj instanceof BedMaterialSpec) {
			BedMaterialSpec rhs = (BedMaterialSpec) obj;
			return new EqualsBuilder()
					.append(this.stationName, rhs.stationName)
					.append(this.parameterCode, rhs.parameterCode)
					.isEquals();
		}
		return false;
	}
	
	public static final String C_SITE_ID = "SITE_ID";
	
	public static final String S_SITE_NAME = "site";
	public static final String C_SITE_NAME = "SITE_NAME";
	public static final String S_BED_VALUE = "bedValue";
	public static final String C_BED_VALUE = "";
	public static final String S_SAMPLE_MASS = "sampleMass";
	public static final String C_SAMPLE_MASS = "MASS";
	public static final String S_SAMPLE_SET = "sampleSet";
	public static final String C_SAMPLE_SET = "SET";
	
}
