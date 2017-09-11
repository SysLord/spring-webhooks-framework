package de.syslord.microservices.webhooks.testbase;

public class Triplet<S1, S2, S3> {

	private S1 s1;

	private S2 s2;

	private S3 s3;

	public Triplet(S1 s1, S2 s2, S3 s3) {
		this.s1 = s1;
		this.s2 = s2;
		this.s3 = s3;
	}

	public S1 getValue0() {
		return s1;
	}

	public S2 getValue1() {
		return s2;
	}

	public S3 getValue2() {
		return s3;
	}

}
