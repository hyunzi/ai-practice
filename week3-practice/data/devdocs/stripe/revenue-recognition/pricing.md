# Pricing

Learn about fees and pricing tiers for Stripe Revenue Recognition.

Stripe charges a fee for every payment that we process. To see what fees we charged, read the [fees report](https://dashboard.stripe.com/balance?type=stripe_fee), which Stripe updates daily. Some fees for line items might take a few days to appear in the report. Learn more about [Stripe Revenue Recognition pricing](https://stripe.com/revenue-recognition#pricing).

The following table explains Revenue Recognition fees.

| Situation                            | Revenue Recognition fees charged                                                                                                                                                                                                                                                                                                       |
| ------------------------------------ | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Successful transactions**          | Stripe applies the fee only when a payment succeeds (for example, when an invoice is paid or when a one-time payment is made).                                                                                                                                                                                                         |
| **Transactions processed by Stripe** | Stripe calculates the fee based on the volume processed, rather than the volume recognized. If a user paid 120 USD for an annual subscription on December 1, Stripe calculates the fee based on the 120 USD volume in December, rather than the 10 USD recognized in December.                                                         |
| **Refunded transactions**            | If you refund the corresponding payment, Stripe won’t refund the Revenue Recognition fee.                                                                                                                                                                                                                                              |
| **Excluded transactions**            | Stripe charges a fee for all transactions we process, which means you incur a fee even if you exclude a transaction from Stripe Revenue Recognition using [custom rules](https://docs.stripe.com/revenue-recognition/rules.md). The transaction still counts toward your volume because Stripe successfully processed the transaction. |
| **Voided transactions**              | If you void an invoice, it won’t appear in your monthly volume and Stripe won’t charge the Revenue Recognition fee.                                                                                                                                                                                                                    |

## Fees for multiple settlement currencies

Each currency has an [equivalent threshold](https://stripe.com/revenue-recognition#pricing). If you have multiple settlement currencies, the combined percentage of thresholds met determines the final fee tier. For each currency, Stripe computes the percentage of volume to the currency’s volume threshold. We call this *percentage-to-threshold volume*. If the total percentage-to-threshold-volume is more than 100, you qualify for a lower-priced tier.

To demonstrate, see the following two example scenarios.

#### Scenario 1

The total percentage-to-threshold-volume is 82%, which means you don’t qualify for the discount.

| Currency | Merchant volume | Threshold volume | Percentage-to-threshold volume |
| -------- | --------------- | ---------------- | ------------------------------ |
| USD      | 80,000          | 100,000          | 80%                            |
| GBP      | 1,000           | 50,000           | 2%                             |

#### Scenario 2

The total percentage-to-threshold-volume is 120%, which means you qualify for the discount.

| Currency | Merchant volume | Threshold volume | Percentage-to-threshold volume |
| -------- | --------------- | ---------------- | ------------------------------ |
| USD      | 80,000          | 100,000          | 80%                            |
| GBP      | 20,000          | 50,000           | 40%                            |

## Features without fees

Stripe doesn’t charge fees for:

- [Rules](https://docs.stripe.com/revenue-recognition/rules.md)
- [Reports](https://docs.stripe.com/revenue-recognition/reports.md)
- [Accounting period controls](https://docs.stripe.com/revenue-recognition/revenue-settings/accounting-period-control.md)
- [Data export](https://docs.stripe.com/revenue-recognition/api.md)
