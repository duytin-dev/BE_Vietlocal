package com.vietlocal.app.utils;

import com.vietlocal.app.domain.entity.BlogPost;
import com.vietlocal.app.domain.entity.Booking;
import com.vietlocal.app.domain.entity.BrandStory;
import com.vietlocal.app.domain.entity.Destination;
import com.vietlocal.app.domain.entity.Guide;
import com.vietlocal.app.domain.entity.Payment;
import com.vietlocal.app.domain.entity.ServiceOffering;
import com.vietlocal.app.dto.response.BlogPostResponse;
import com.vietlocal.app.dto.response.BookingResponse;
import com.vietlocal.app.dto.response.BrandStoryResponse;
import com.vietlocal.app.dto.response.DestinationResponse;
import com.vietlocal.app.dto.response.GuideResponse;
import com.vietlocal.app.dto.response.PaymentQrResponse;

public final class EntityMapper {

	private EntityMapper() {
	}

	public static DestinationResponse toDestinationResponse(Destination d) {
		return DestinationResponse.builder()
				.id(d.getId())
				.name(d.getName())
				.slug(d.getSlug())
				.region(d.getRegion())
				.summary(d.getSummary())
				.description(d.getDescription())
				.imageUrl(d.getImageUrl())
				.featured(d.isFeatured())
				.build();
	}

	public static BlogPostResponse toBlogPostResponse(BlogPost b) {
		BlogPostResponse.BlogPostResponseBuilder builder = BlogPostResponse.builder()
				.id(b.getId())
				.title(b.getTitle())
				.slug(b.getSlug())
				.excerpt(b.getExcerpt())
				.content(b.getContent())
				.coverImageUrl(b.getCoverImageUrl())
				.publishedAt(b.getPublishedAt());
		if (b.getDestination() != null) {
			builder.destinationId(b.getDestination().getId())
					.destinationSlug(b.getDestination().getSlug())
					.destinationName(b.getDestination().getName());
		}
		return builder.build();
	}

	public static com.vietlocal.app.dto.response.ServiceResponse toServiceResponse(ServiceOffering s) {
		return com.vietlocal.app.dto.response.ServiceResponse.builder()
				.id(s.getId())
				.name(s.getName())
				.slug(s.getSlug())
				.description(s.getDescription())
				.iconUrl(s.getIconUrl())
				.sortOrder(s.getSortOrder())
				.build();
	}

	public static GuideResponse toGuideResponse(Guide g) {
		return toGuideResponse(g, true);
	}

	public static GuideResponse toGuideResponse(Guide g, boolean available) {
		return GuideResponse.builder()
				.id(g.getId())
				.name(g.getName())
				.slug(g.getSlug())
				.tier(g.getTier())
				.bio(g.getBio())
				.styleDescription(g.getStyleDescription())
				.imageUrl(g.getImageUrl())
				.languages(g.getLanguages())
				.rating(g.getRating())
				.pricePerDay(g.getPricePerDay())
				.available(available)
				.build();
	}

	public static BrandStoryResponse toBrandStoryResponse(BrandStory b) {
		return BrandStoryResponse.builder()
				.id(b.getId())
				.title(b.getTitle())
				.content(b.getContent())
				.heroImageUrl(b.getHeroImageUrl())
				.tagline(b.getTagline())
				.build();
	}

	public static BookingResponse toBookingResponse(Booking b) {
		return BookingResponse.builder()
				.id(b.getId())
				.customerName(b.getCustomerName())
				.email(b.getEmail())
				.phone(b.getPhone())
				.guideId(b.getGuide() != null ? b.getGuide().getId() : null)
				.guideName(b.getGuide() != null ? b.getGuide().getName() : null)
				.destinationName(b.getDestinationName())
				.tripTitle(b.getTripTitle())
				.itinerarySummary(b.getItinerarySummary())
				.customerNotes(b.getCustomerNotes())
				.estimatedDays(b.getEstimatedDays())
				.totalAmount(b.getTotalAmount())
				.status(b.getStatus())
				.createdAt(b.getCreatedAt())
				.build();
	}

	public static PaymentQrResponse toPaymentQrResponse(Payment p) {
		return PaymentQrResponse.builder()
				.bookingId(p.getBooking().getId())
				.paymentId(p.getId())
				.amount(p.getAmount())
				.currency(p.getCurrency())
				.qrCodeUrl(p.getQrCodeUrl())
				.transactionRef(p.getTransactionRef())
				.status(p.getStatus())
				.build();
	}
}
