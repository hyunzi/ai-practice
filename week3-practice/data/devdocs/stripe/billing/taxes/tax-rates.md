# Tax rates

Learn how to collect and report taxes with tax rate objects.

Stripe allows you to define any number of tax rates and apply them to *invoices* (Invoices are statements of amounts owed by a customer. They track the status of payments from draft through paid or otherwise finalized. Subscriptions automatically generate invoices, or you can manually create a one-off invoice), *subscriptions* (A Subscription represents the product details associated with the plan that your customer subscribes to. Allows you to charge the customer on a recurring basis), and one time payments when using Checkout. However, we won?�t automatically set them on your behalf.

If you?�re looking for automated tax calculation where you don?�t need to define the rates, use [Stripe Tax](https://docs.stripe.com/tax.md).

When applying tax rates, Stripe calculates the [total tax amount](https://docs.stripe.com/billing/taxes/tax-rates.md#tax-amounts) per tax rate, and summarizes it in a table that you can export into tax summary reports.

## Creating tax rates

If you?�re working with a small number of tax rates, you can manage and create them in the [Dashboard](https://dashboard.stripe.com/test/tax-rates). After creating tax rates, you can apply them to [invoices](https://docs.stripe.com/invoicing/taxes/tax-rates.md), [subscriptions](https://docs.stripe.com/billing/taxes.md), and [one-time payments](https://docs.stripe.com/payments/checkout/taxes.md) or [subscriptions](https://docs.stripe.com/billing/taxes/collect-taxes.md?tax-calculation=tax-rates#adding-tax-rates-to-checkout) created through Stripe Checkout.

Create a catalog of tax rates that meet the requirements for the jurisdictions that you do business in. For example, if you operate in Europe, you might want to create a catalog of tax rates for OSS VAT.

#### Creating tax rates through the API

The following example demonstrates how you can create a tax rate through the API.

```curl
curl https://api.stripe.com/v1/tax_rates \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d display_name="Sales Tax" \
  -d inclusive=false \
  -d percentage="7.25" \
  -d country=US \
  -d state=CA \
  -d jurisdiction="US - CA" \
  -d description="CA Sales Tax"
```

```cli
stripe tax_rates create  \
  --display-name="Sales Tax" \
  --inclusive=false \
  --percentage=7.25 \
  --country=US \
  --state=CA \
  --jurisdiction="US - CA" \
  --description="CA Sales Tax"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

tax_rate = client.v1.tax_rates.create({
  display_name: 'Sales Tax',
  inclusive: false,
  percentage: 7.25,
  country: 'US',
  state: 'CA',
  jurisdiction: 'US - CA',
  description: 'CA Sales Tax',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
tax_rate = client.v1.tax_rates.create({
  "display_name": "Sales Tax",
  "inclusive": False,
  "percentage": 7.25,
  "country": "US",
  "state": "CA",
  "jurisdiction": "US - CA",
  "description": "CA Sales Tax",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$taxRate = $stripe->taxRates->create([
  'display_name' => 'Sales Tax',
  'inclusive' => false,
  'percentage' => 7.25,
  'country' => 'US',
  'state' => 'CA',
  'jurisdiction' => 'US - CA',
  'description' => 'CA Sales Tax',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

TaxRateCreateParams params =
  TaxRateCreateParams.builder()
    .setDisplayName("Sales Tax")
    .setInclusive(false)
    .setPercentage(new BigDecimal(7.25))
    .setCountry("US")
    .setState("CA")
    .setJurisdiction("US - CA")
    .setDescription("CA Sales Tax")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
TaxRate taxRate = client.v1().taxRates().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const taxRate = await stripe.taxRates.create({
  display_name: 'Sales Tax',
  inclusive: false,
  percentage: 7.25,
  country: 'US',
  state: 'CA',
  jurisdiction: 'US - CA',
  description: 'CA Sales Tax',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TaxRateCreateParams{
  DisplayName: stripe.String("Sales Tax"),
  Inclusive: stripe.Bool(false),
  Percentage: stripe.Float64(7.25),
  Country: stripe.String("US"),
  State: stripe.String("CA"),
  Jurisdiction: stripe.String("US - CA"),
  Description: stripe.String("CA Sales Tax"),
}
result, err := sc.V1TaxRates.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new TaxRateCreateOptions
{
    DisplayName = "Sales Tax",
    Inclusive = false,
    Percentage = 7.25M,
    Country = "US",
    State = "CA",
    Jurisdiction = "US - CA",
    Description = "CA Sales Tax",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.TaxRates;
TaxRate taxRate = service.Create(options);
```

Required properties:

- The `display_name` appears on your customer?�s invoice, and is usually a short name that describes the specific type of tax, such as `Sales`, `VAT`, or `GST`.
- The `inclusive` property determines whether the tax `percentage` is added to, or included in, the overall amount.
- The `percentage` is a number (up to 4 decimal places) that represents the tax percentage to be collected.

Optional properties:

- The optional `country` property is a valid [two-letter ISO country code](https://www.nationsonline.org/oneworld/country_code_list.htm). Some countries (for example, United States) require an additional two-letter `state` property. Use these properties to apply dynamic tax rates based on your customer?�s billing or shipping address in Checkout Sessions.
- The optional `jurisdiction` property represents the tax jurisdiction of the tax rate and can be used to differentiate between tax rates of the same percentage. `jurisdiction` appears on your customer?�s invoice. In the Dashboard, jurisdiction appears as the *Region* label for the tax rate.
- You can also store additional details in the `description`. Your customers won?�t see this property.

You can?�t change the percentage, country, or state properties after you set them, and you can only set them when you create the tax rate. This ensures that existing subscriptions and invoices using tax rates aren?�t affected. If you need to update these properties, create a new tax rate and archive the old object.

## Inclusive versus exclusive tax 

Tax rates can either be exclusive or inclusive. An *exclusive* tax is not included in the invoice subtotal, whereas an *inclusive* tax is.

The following table illustrates a 25% tax rate modifying the total amount due, depending on whether it?�s exclusive or inclusive.

| Tax           | Subtotal | Tax due                               | Total                     |
| ------------- | -------- | ------------------------------------- | ------------------------- |
| 25% Exclusive | $5.00    | $1.25                                 | **$6.25** ($5.00 + $1.25) |
| 25% Inclusive | $5.00    | $1.00 (already included in the total) | **$5.00** ($4.00 + $1.00) |

## Tax exempt and reverse charge

You can set the exemption status for a *Customer* (Customer objects represent customers of your business. They let you reuse payment methods and give you the ability to track multiple payments) to either exempt or reverse.

In both cases, no tax is calculated on the invoice.

In the case where the customer is liable for the tax (that is, under the reverse-charge procedure within EU VAT), set the exemption status to `reverse`. The invoice and receipt PDF includes the text **?�Reverse charge??*.

[Download example reverse-charge invoice PDF](https://d37ugbyn3rpeym.cloudfront.net/docs/files/billing/taxes/example-reverse-charge.pdf)

If a one time payment is performed using Checkout, the exemption status is captured as [customer_details](https://docs.stripe.com/api/checkout/sessions/object.md#checkout_session_object-customer_details) in the Checkout Session object.

If the customer is either exempt or reverse, for invoices with *inclusive* tax rates, the buyer pays the `unit_amount` price *minus* the tax that would?�ve been paid had the user not been exempt or reverse. In other words, manual tax rates effectively calculate taxes as if the user weren?�t exempt and then ?�backs out??the taxes.

The following table illustrates a 10% tax rate modifying the total amount due for an exempt or reverse customer. The first row is an example of ?�backed out??taxes.

| Tax           | Amount | Tax due | Total                                                        |
| ------------- | ------ | ------- | ------------------------------------------------------------ |
| 10% inclusive | 100    | $0      | $90.91 (inclusive tax of $9.09 is subtracted from the price) |
| 10% exclusive | 100    | $0      | $100                                                         |

## Using multiple tax rates 

You can apply Tax rates to line items or set them as a default for all line items in an invoice. You can set up to ten tax rates per line item. When you set tax rates on both a line item and the invoice, the rates for that invoice don?�t apply to that line item.

For example, this invoice has two overall tax rates of 9.975% and 5%:

| Invoice     | Item tax rate | Overall invoice tax rate | Item tax rate (Effective) |
| ----------- | ------------- | ------------------------ | ------------------------- |
| Line item 1 | (none)        | 9.975% and 5%            | 9.975% and 5%             |
| Line item 2 | 10%           | 9.975% and 5%            | 10%                       |
| Line item 3 | 1% and 2%     | 9.975% and 5%            | 1% and 2%                 |

## Tax amounts 

When you apply tax rates to an invoice, they?�re aggregated into the [total_tax_amounts](https://docs.stripe.com/api/invoices/object.md#invoice_object-total_tax_amounts) attribute. This attribute represents the sum of all tax amounts, per tax rate, over the entire invoice.

For example, here?�s an invoice where two line items have two different rates:

| Invoice              | Amount | Tax Rate   | Tax Amount | Totals |
| -------------------- | ------ | ---------- | ---------- | ------ |
| Line item 1          | $5.00  | 5% (excl)  | $0.25      | ??     |
| Line item 2          | $10.00 | 10% (excl) | $1.00      | ??     |
| **Total Tax Amount** | ??     | ??         | $1.25      | ??     |
| **Total**            | $15.00 | ??         | ??         | $16.25 |

[Download example invoice PDF](https://d37ugbyn3rpeym.cloudfront.net/docs/files/billing/taxes/example-tax-amounts.pdf)

## Rounding

When determining tax amounts, you can do either of the following:

- Round at the invoice line item level to the [smallest currency unit](https://docs.stripe.com/currencies.md#zero-decimal) before summing individual tax amounts across the entire invoice. We refer to this as ?�line item level??
- Sum up all individual taxable amounts unrounded per tax rate. Combine them to a subtotal, apply the tax rate on the subtotal, and then round. We refer to this as ?�invoice level??

Select this configuration on the [invoice settings](https://dashboard.stripe.com/settings/billing/invoice) page in the Dashboard. The rounding configuration is only available for invoices with manual tax rates. Invoices with automatic Stripe tax always sum up the tax amounts first and then round.

#### Line item level

| Name              | Amount    | Inclusive Tax Rate | Taxable Amount (before rounding) | Tax Amount (before rounding) | Tax Amount (after rounding) |
| ----------------- | --------- | ------------------ | -------------------------------- | ---------------------------- | --------------------------- |
| Line Item 1       | $1000.00  | 10%                | $909.0909                        | $90.9091                     | $90.91                      |
| Line Item 2       | $50.00    | 10%                | $45.4545                         | $4.5455                      | $4.55                       |
| Subtotal          | $1,050.00 | ??                 | ??                               | ??                           | ??                          |
| Total Tax Amounts | ??        | ??                 | ??                               | ??                           | $95.46                      |
| Total rounded     | $1,050.00 | ??                 | $954.54                          | ??                           | $95.46                      |

[Download example line item level rounding invoice PDF](https://d37ugbyn3rpeym.cloudfront.net/docs/files/billing/taxes/example-line-item-level-rounding.pdf)

#### Invoice level

| Name              | Amount    | Inclusive Tax Rate | Taxable Amount (before rounding) | Tax Amount (before rounding) | Tax Amount (after rounding) |
| ----------------- | --------- | ------------------ | -------------------------------- | ---------------------------- | --------------------------- |
| Line Item 1       | $1000.00  | 10%                | $909.0909                        | ??                           | ??                          |
| Line Item 2       | $50.00    | 10%                | $45.4545                         | ??                           | ??                          |
| Subtotal          | $1,050.00 | 10%                | $954.5455                        | ??                           | ??                          |
| Total Tax Amounts | $1,050.00 | 10%                | $954.5455                        | $95.45                       | $95.45                      |
| Total rounded     | $1,050.00 | ??                 | $954.55                          | ??                           | $95.45                      |

[Download example invoice level rounding invoice PDF](https://d37ugbyn3rpeym.cloudfront.net/docs/files/billing/taxes/example-invoice-level-rounding.pdf)

## Discounts

Discounts are usually applied before tax, but this isn?�t always the case.

Reading each line left-to-right, noting the formula applied (in the table header), you can trace the values as they?�re applied to the final, total amount.

### Exclusive tax discount example

Stripe always applies discounts before exclusive tax.

This example shows how we apply discounts to an exclusive tax rate.

| Invoice Item | Amount     | Discount % | Discount $          | Post Discount        | Tax Rate | Tax $                    | **Total**             |
| ------------ | ---------- | ---------- | ------------------- | -------------------- | -------- | ------------------------ | --------------------- |
| **Formula**  | ??         | ??         | `Amount * Discount` | `Amount - Discount$` | ??       | `PostDiscount * TaxRate` | `PostDiscount + Tax$` |
| Line item 1  | $5.00      | 10%        | $0.50               | $4.50                | 5% exl.  | $0.23                    | **$4.73**             |
| Line item 2  | $10.00     | 10%        | $1.00               | $9.00                | 5% exl.  | $0.45                    | **$9.45**             |
| **Total**    | **$15.00** |            | **$1.50**           | **$13.50**           |          | **$0.68 (@ 5% exl.)**    | **$14.18**            |

[Download example discounts invoice PDF](https://d37ugbyn3rpeym.cloudfront.net/docs/files/billing/taxes/example-exclusive-tax-with-discount.pdf)

### Inclusive tax discount example

When tax rates are inclusive, Stripe Tax applies discounts to the original amount first. Then, we recalculate taxes based on the remaining amount. This reduction has the side effect of reducing the tax amount due.

| Invoice Item | Amount     | Discount % | Discount $           | Post Discount        | Tax Rate | Tax $ (Included)                              | **Total**      |
| ------------ | ---------- | ---------- | -------------------- | -------------------- | -------- | --------------------------------------------- | -------------- |
| **Formula**  | ??         | ??         | `Amount * Discount%` | `Amount - Discount$` | ??       | `PostDiscount - PostDiscount / (1 + TaxRate)` | `PostDiscount` |
| Line item 1  | $5.00      | 10%        | $0.50                | $4.50                | 5% incl. | $0.21                                         | **$4.50**      |
| Line item 2  | $10.00     | 10%        | $1.00                | $9.00                | 5% incl. | $0.43                                         | **$9.00**      |
| **Total**    | **$15.00** | **??*      | **$1.50**            | **$13.50**           | **??*    | **$0.64 (@ 5% incl.)**                        | **$13.50**     |

[Download example invoice PDF](https://d37ugbyn3rpeym.cloudfront.net/docs/files/billing/taxes/example-inclusive-tax-with-discount.pdf)

### Both inclusive and exclusive tax with discount example

In the case where you have both inclusive and exclusive tax, the two rules apply together in the following steps for every line item:

1. We calculate the inclusive tax amount based on the post-discounted amount by multiplying by the inclusive tax rate.
1. We calculate the exclusive tax amount by multiplying the exclusive tax rate by the post-discounted amount, less the inclusive tax amount.
1. We calculate the total amount due by summing the post-discounted amount and the exclusive tax amount (calculated in step 2).

| Invoice Item | Amount     | Discount % | Discount $           | Post Discount        | Inclusive Tax Rate | Inclusive Tax $                               | Post Discount, Less Incl. Tax  | Exclusive Tax Rate | Exclusive Tax $                | **Total**                 |
| ------------ | ---------- | ---------- | -------------------- | -------------------- | ------------------ | --------------------------------------------- | ------------------------------ | ------------------ | ------------------------------ | ------------------------- |
| **Formula**  | ??         | ??         | `Amount * Discount%` | `Amount - Discount$` | ??                 | `PostDiscount - PostDiscount / (1 + TaxRate)` | `PostDiscount - InclusiveTax$` | ??                 | `PostDiscLessIncTax * TaxRate` | `PostDiscount + ExclTax$` |
| Line item 1  | $5.00      | 10%        | $0.50                | $4.50                | 5% incl.           | $0.21                                         | $4.29                          | 7% excl.           | $0.30                          | **$4.80**                 |
| Line item 2  | $10.00     | 10%        | $1.00                | $9.00                | 5% incl.           | $0.43                                         | $8.57                          | 7% excl.           | $0.60                          | **$9.60**                 |
| **Total**    | **$15.00** | **??*      | **$1.50**            | **$13.50**           | **??*              | **$0.64 (@ 5% incl.)**                        | **$12.86**                     | **??*              | **$0.90 (@ 7% excl.)**         | **$14.40**                |

[Download example invoice PDF](https://d37ugbyn3rpeym.cloudfront.net/docs/files/billing/taxes/example-inclusive-and-exclusive-tax-with-discount.pdf)

## Tax reporting and remittance 

Any business collecting taxes ultimately needs to remit tax to the appropriate government.

See [Tax reporting and filing](https://docs.stripe.com/tax/reports.md) to learn more.

### Data exports

From the Dashboard?�s [Tax Rates list](https://dashboard.stripe.com/test/tax-rates/) page, you can export data files required for tax reporting calculations.

Stripe Billing provides two different levels of tax report export files:

- **Invoice line item tax export** ??A lower-level export, this includes details down to the line item level, including per-line-item tax rates, inclusive/exclusive, amounts, and so on.
- **Invoice totals export** ??Shows the aggregate tax collected on the invoice as a whole, including adjustments for any refunds.

For remittance reporting, use the line-item tax export to sum all amounts paid for all tax rates used. To factor in any refunds you will also need to pivot against the Invoice totals export.

## Migrate to tax rates 

If you?�re using the deprecated `tax_percent`, `tax_info`, `tax_info_verification`, and `business_vat_id` fields, review the following options to migrate to tax rates and [Customer Tax IDs](https://docs.stripe.com/billing/customer/tax-ids.md) for better tax collection and reporting (remittance) tools.

### Existing tax percent use cases have been migrated to tax rates

Existing `tax_percent` uses have been automatically converted into [tax rates](https://docs.stripe.com/api/tax_rates.md), and your invoices and subscriptions have been updated to reference the new objects through [default_tax_rates](https://docs.stripe.com/api/invoices/create.md#create_invoice-default_tax_rates).

This means that if you had previously been setting a `tax_percent` of `15%` on your invoices, Stripe has created a new `15%` tax rate object for you (although it lacks details such as a customer facing display name or a jurisdiction). If you continue to set the `tax_percent` to `15%`, Stripe dynamically creates a 15% tax rate for you to aid your migration. This works exactly as it had before.

You can manage your full list of tax rates in the Dashboard?�s [tax rates](https://dashboard.stripe.com/tax-rates) page.

### Migration options

For new invoices or subscriptions, we recommend performing the [full](https://docs.stripe.com/billing/taxes/tax-rates.md#full) update to use tax rates.

#### No action 

If you take no action, your integration continues to work as it does today. As mentioned above, existing uses of `tax_percent` are made to look as if they used tax rates.

As your tax rates lack a [display_name](https://docs.stripe.com/api/tax_rates/object.md#tax_rate_object-display_name) and [jurisdiction](https://docs.stripe.com/api/tax_rates/object.md#tax_rate_object-jurisdiction), tax reporting might not be very useful. Invoices and receipts render a generic name for these rates?�“Tax??

#### Minimal update with medium benefits 

Use the Dashboard to edit pre-existing tax rates so taxes work for pre-existing invoices.

1. For tax rates that have been migrated for you, edit the [display_name](https://docs.stripe.com/api/tax_rates/object.md#tax_rate_object-display_name) to have a useful user-facing name. Display names are displayed to your customers on generated invoices and receipts (for example, ?�UST??for German VAT and ?�HST??for Ontario?�s Harmonized Sales Tax).
1. Set the [jurisdiction](https://docs.stripe.com/api/tax_rates/object.md#tax_rate_object-jurisdiction) to store an associated tax jurisdiction (for example, ?�DE??for Germany or ?�NL Amsterdam??for the city of Amsterdam).

Invoices and receipts show the `display_name` of tax rates. When determining how much tax to remit, you can group by jurisdiction.

#### Full update and benefits 

We no longer recommend using the `tax_percent` field for new invoices, and to use tax rates instead. Apply tax rates to [invoices](https://docs.stripe.com/invoicing/taxes/tax-rates.md) and [subscriptions](https://docs.stripe.com/billing/taxes.md). This allows you to add multiple tax rates per line item and invoice, display the correct name for tax rates and summaries on generated invoices and receipts, and improved tax reporting.

### Customer Tax IDs

The Customer?�s `tax_info`, `tax_info_verification`, and `business_vat_id` fields are deprecated in favor of [Customer Tax IDs](https://docs.stripe.com/api/customers/object.md#customer_object-tax_ids). The [Tax ID](https://docs.stripe.com/api/customer_tax_ids.md) object provides:

- Multiple tax IDs on a Customer.
- Support for more tax ID types, such as EU VAT, NZ GST, and AU ABN.
- Automatic validation of EU VAT numbers against the [European Commission?�s VAT Information Exchange System (VIES)](http://ec.europa.eu/taxation_customs/vies/) database.
- Automatic validation of Australian Business Numbers (ABNs) against the [Australian Business Register (ABR)](https://abr.gov.au/).
- Associate a country with a tax ID (for example, a German EU VAT number).

See [Customer Tax IDs](https://docs.stripe.com/billing/customer/tax-ids.md) for more information.
