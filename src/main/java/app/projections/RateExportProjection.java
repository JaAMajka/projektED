package app.projections;

public interface RateExportProjection {
    Long getRateId();
    Long getUserId();
    Long getCafeId();
    Integer getBeverageScore();
    Integer getServiceScore();
    Integer getAtmosphereScore();
    String getUserProfile();
}