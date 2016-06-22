package de.isnow.rest;


public class JsonViews {
	/*
	 * Jackson classes to tag properties for different REST
	 * endpoints. This allows us to define scenarios where more or
	 * less information should be transferred without a hierarchy
	 * of Data Transport Objects - similar to database views.
	 * 
	 * http://wiki.fasterxml.com/JacksonJsonViews
	 * 
	 * In the REST service definitions, configure your ObjectMapper:
	 * 		mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
	 * 
	 * To use views in a REST service definition:
	 *		ObjectWriter w = mapper.writerWithView(JsonViews.MediumJsonView.class);
	 *		return Response.ok(w.writeValueAsString(myObject)).build(); 
	 */
	
	// for a select list of items, return only the required properties
	public static class CoarseJsonView { }
	
	// for a interactive table of items, return the properties from 
	// CoarseJsonView and add more
	public static class ShortJsonView extends CoarseJsonView { }
	
	/* for another use case, return the properties from 
	 * ShortJsonView and add more
	 */
	public static class MediumJsonView extends ShortJsonView{ }
	
	/* for an inspector, return the properties from 
	 * CoarseJsonView and those from MediumJsonView and add more
	 */
	public static class FullJsonView extends MediumJsonView{ }
	
	public static Class<? extends JsonViews.CoarseJsonView> parseGranularity (String granularityStr) {
		if ((null == granularityStr) || (granularityStr.isEmpty())) {
			return JsonViews.MediumJsonView.class;
		}
		if (granularityStr.equalsIgnoreCase("coarse")) {
			return JsonViews.CoarseJsonView.class;
		} else if (granularityStr.equalsIgnoreCase("short")) {
			return JsonViews.ShortJsonView.class;
		} else if (granularityStr.equalsIgnoreCase("medium")) {
			return JsonViews.MediumJsonView.class;
		} else if (granularityStr.equalsIgnoreCase("full")) {
			return JsonViews.FullJsonView.class;
		}
		return JsonViews.MediumJsonView.class;
	}
}
