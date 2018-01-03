package travel;

import java.util.Comparator;

/**
 * Various comparators for Itinerary.
 * @author Mathu
 *
 */
public enum ItineraryComparator implements Comparator<Itinerary> {
  TRAVELTIME_SORT {
    public int compare(Itinerary i1, Itinerary i2) {
      return (Long.valueOf(i1.getTravelTime()).compareTo(i2.getTravelTime()));
    }
  },

  COST_SORT {
    public int compare(Itinerary i1, Itinerary i2) {
      return (Double.valueOf(i1.getCost())).compareTo(i2.getCost());
    }
  };
}
