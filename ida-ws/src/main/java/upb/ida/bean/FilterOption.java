package upb.ida.bean;

public class FilterOption {
	private String fieldName;
	private FilterSort sort;
	private Integer fromSeq;
	private Integer toSeq;
	/**
	 * For only field level sorting
	 * @param fieldName - name of the field
	 * @param sort - direction of the sort
	 */
	public FilterOption(String fieldName, FilterSort sort) {
		super();
		this.fieldName = fieldName;
		this.sort = sort;
	}
	/**
	 * For custom sub selection
	 * @param fieldName - name of the field
	 * @param fromSeq - Index of the starting record (inclusive)
	 * @param toSeq - Index of the end record (exclusive)
	 */
	public FilterOption(String fieldName, Integer fromSeq, Integer toSeq) {
		super();
		this.fieldName = fieldName;
		this.fromSeq = fromSeq;
		this.toSeq = toSeq;
	}
	/**
	 * For sorting and custom sub selection together
	 * @param fieldName - name of the field
	 * @param sort - direction of the sort
	 * @param fromSeq - Index of the starting record (inclusive)
	 * @param toSeq - Index of the end record (exclusive)
	 */
	public FilterOption(String fieldName, FilterSort sort, Integer fromSeq, Integer toSeq) {
		super();
		this.fieldName = fieldName;
		this.sort = sort;
		this.fromSeq = fromSeq;
		this.toSeq = toSeq;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public FilterSort getSort() {
		return sort;
	}

	public void setSort(FilterSort sort) {
		this.sort = sort;
	}

	public Integer getFromSeq() {
		return fromSeq;
	}

	public void setFromSeq(Integer fromSeq) {
		this.fromSeq = fromSeq;
	}

	public Integer getToSeq() {
		return toSeq;
	}

	public void setToSeq(Integer toSeq) {
		this.toSeq = toSeq;
	}
	
}
