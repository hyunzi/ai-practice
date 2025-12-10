# Tax for marketplaces

Learn about tax requirements for platforms and marketplaces, and how to enable Stripe Tax to collect tax on transactions when the Connect platform is liable.

## Tax requirements for platforms and marketplaces

Many countries and US states require marketplace operators to collect sales tax and VAT on their facilitated sales. The US refers to these businesses as marketplace facilitators, while other regions, such as Europe, might refer to them as deemed sellers.

As a marketplace operator, your tax collection requirements differ depending on the country or state. However, if your electronic interface enables transactions between buyers and sellers and you directly or indirectly collect customer payments, you might need to fulfill tax collection responsibilities.

If your businesses operates a marketplace or platform, you must first determine whether they qualify as a marketplace facilitator or a deemed seller, then make sure that they maintain tax compliance. If you’re unsure about your business’s tax requirements, consult a tax advisor.

If your business operates a marketplace and wants to collect tax on sales facilitated through this marketplace, refer to details below to enable Stripe Tax for marketplaces.

## Enable Stripe Tax for marketplaces

Stripe Tax enables businesses to calculate, collect, and file indirect taxes in over [100 countries](https://docs.stripe.com/tax/supported-countries.md), across hundreds of product categories.

Use this guide if your platform is responsible for collecting, filing, and reporting taxes.

> We use the platform’s head office location, preset tax code, and tax registrations to calculate taxes. However, we don’t use the connected account information for tax purposes.

1. [Configure your platform account for tax collection](https://docs.stripe.com/tax/tax-for-marketplaces.md#set-up)
1. (Optional) [Assign tax codes to product catalog](https://docs.stripe.com/tax/tax-for-marketplaces.md#assign-product-tax-codes)
1. [Integrate tax calculation and collection](https://docs.stripe.com/tax/tax-for-marketplaces.md#enable-tax-collection)
1. [Withhold the collected tax amount](https://docs.stripe.com/tax/tax-for-marketplaces.md#tax-withholding)
1. [Access Stripe Tax reports](https://docs.stripe.com/tax/tax-for-marketplaces.md#access-reports)

## Configure your platform account for tax collection

To collect taxes, you need the platform account’s tax settings and registrations.

### Use the Stripe Dashboard

[Use the Stripe Dashboard](https://docs.stripe.com/tax/set-up.md) to specify your head office location, preset tax code, and tax registrations.

### Use the Stripe API

Use the [Tax Settings API](https://docs.stripe.com/tax/settings-api.md#updating-settings) to set your head office location and other default values and the [Tax Registrations API](https://docs.stripe.com/tax/registrations-api.md#adding-registration) to add tax registrations for the locations where you have tax obligations.

## Assign tax codes to your product catalog

To calculate taxes, Stripe Tax requires that you classify products into tax codes. You can do so by supplying [a preset tax code](https://docs.stripe.com/tax/products-prices-tax-codes-tax-behavior.md#preset-tax-codes) for the platform account, which might be sufficient if you typically sell a single category of items or services.

Additionally, you can [map tax codes to each product](https://docs.stripe.com/tax/products-prices-tax-codes-tax-behavior.md#tax-code-on-product) to give you more control over tax categorization. You might have to map each product that a seller sets up on your marketplace. You can find a list of supported tax codes from [available tax codes](https://docs.stripe.com/tax/tax-codes.md) or retrieve it from the Stripe [Tax Code API](https://docs.stripe.com/api/tax_codes.md).

## Integrate tax calculation and collection

You need to integrate with Stripe Tax to estimate taxes as part of your checkout flow.

### Payment Links

### Payment Links for one-time payments

Pick one of the currently supported charge types that allow your platform account to be *liable for tax* (The responsibility for collecting and reporting taxes for transactions in a Connect integration. It can belong to the platform or to connected accounts, depending on your business model, government regulations, and individual transaction details) with [Stripe Payment Links](https://docs.stripe.com/tax/payment-links.md):

#### Direct charges

This charge type isn’t supported for use cases involving a connected account where the platform is *liable for tax* (The responsibility for collecting and reporting taxes for transactions in a Connect integration. It can belong to the platform or to connected accounts, depending on your business model, government regulations, and individual transaction details).

#### Destination charges

For the Payment Links API calls:

- Include [automatic_tax[liability]](https://docs.stripe.com/api/payment_links/payment_links/create.md#create_payment_link-automatic_tax-liability) with `type=self`.
- Include [transfer_data[destination]](https://docs.stripe.com/api/payment_links/payment_links/create.md#create_payment_link-transfer_data) with the value of the connected account ID.
- (Optional) If you’re [automatically sending invoices](https://docs.stripe.com/payment-links/post-payment.md#automatically-send-paid-invoices), include [invoice_creation[invoice_data][issuer]](https://docs.stripe.com/api/payment_links/payment_links/create.md#create_payment_link-invoice_creation-invoice_data-issuer) with `type=self`.
- (Optional) If the connected account is the [settlement merchant](https://docs.stripe.com/connect/destination-charges.md#settlement-merchant), include [on_behalf_of](https://docs.stripe.com/api/payment_links/payment_links/create.md#create_payment_link-on_behalf_of) with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/payment_links \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=2 \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=self \
  -d "transfer_data[destination]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "invoice_creation[enabled]"=true \
  -d "invoice_creation[invoice_data][issuer][type]"=self
```

```cli
stripe payment_links create  \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=2 \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=self \
  -d "transfer_data[destination]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "invoice_creation[enabled]"=true \
  -d "invoice_creation[invoice_data][issuer][type]"=self
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

payment_link = client.v1.payment_links.create({
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 2,
    },
  ],
  automatic_tax: {
    enabled: true,
    liability: {type: 'self'},
  },
  transfer_data: {destination: '{{CONNECTEDACCOUNT_ID}}'},
  invoice_creation: {
    enabled: true,
    invoice_data: {issuer: {type: 'self'}},
  },
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
payment_link = client.v1.payment_links.create({
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 2}],
  "automatic_tax": {"enabled": True, "liability": {"type": "self"}},
  "transfer_data": {"destination": "{{CONNECTEDACCOUNT_ID}}"},
  "invoice_creation": {"enabled": True, "invoice_data": {"issuer": {"type": "self"}}},
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$paymentLink = $stripe->paymentLinks->create([
  'line_items' => [
    [
      'price' => '{{PRICE_ID}}',
      'quantity' => 2,
    ],
  ],
  'automatic_tax' => [
    'enabled' => true,
    'liability' => ['type' => 'self'],
  ],
  'transfer_data' => ['destination' => '{{CONNECTEDACCOUNT_ID}}'],
  'invoice_creation' => [
    'enabled' => true,
    'invoice_data' => ['issuer' => ['type' => 'self']],
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

PaymentLinkCreateParams params =
  PaymentLinkCreateParams.builder()
    .addLineItem(
      PaymentLinkCreateParams.LineItem.builder()
        .setPrice("{{PRICE_ID}}")
        .setQuantity(2L)
        .build()
    )
    .setAutomaticTax(
      PaymentLinkCreateParams.AutomaticTax.builder()
        .setEnabled(true)
        .setLiability(
          PaymentLinkCreateParams.AutomaticTax.Liability.builder()
            .setType(PaymentLinkCreateParams.AutomaticTax.Liability.Type.SELF)
            .build()
        )
        .build()
    )
    .setTransferData(
      PaymentLinkCreateParams.TransferData.builder()
        .setDestination("{{CONNECTEDACCOUNT_ID}}")
        .build()
    )
    .setInvoiceCreation(
      PaymentLinkCreateParams.InvoiceCreation.builder()
        .setEnabled(true)
        .setInvoiceData(
          PaymentLinkCreateParams.InvoiceCreation.InvoiceData.builder()
            .setIssuer(
              PaymentLinkCreateParams.InvoiceCreation.InvoiceData.Issuer.builder()
                .setType(
                  PaymentLinkCreateParams.InvoiceCreation.InvoiceData.Issuer.Type.SELF
                )
                .build()
            )
            .build()
        )
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
PaymentLink paymentLink = client.v1().paymentLinks().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const paymentLink = await stripe.paymentLinks.create({
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 2,
    },
  ],
  automatic_tax: {
    enabled: true,
    liability: {
      type: 'self',
    },
  },
  transfer_data: {
    destination: '{{CONNECTEDACCOUNT_ID}}',
  },
  invoice_creation: {
    enabled: true,
    invoice_data: {
      issuer: {
        type: 'self',
      },
    },
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.PaymentLinkCreateParams{
  LineItems: []*stripe.PaymentLinkCreateLineItemParams{
    &stripe.PaymentLinkCreateLineItemParams{
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(2),
    },
  },
  AutomaticTax: &stripe.PaymentLinkCreateAutomaticTaxParams{
    Enabled: stripe.Bool(true),
    Liability: &stripe.PaymentLinkCreateAutomaticTaxLiabilityParams{
      Type: stripe.String(stripe.PaymentLinkAutomaticTaxLiabilityTypeSelf),
    },
  },
  TransferData: &stripe.PaymentLinkCreateTransferDataParams{
    Destination: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
  },
  InvoiceCreation: &stripe.PaymentLinkCreateInvoiceCreationParams{
    Enabled: stripe.Bool(true),
    InvoiceData: &stripe.PaymentLinkCreateInvoiceCreationInvoiceDataParams{
      Issuer: &stripe.PaymentLinkCreateInvoiceCreationInvoiceDataIssuerParams{
        Type: stripe.String(stripe.PaymentLinkInvoiceCreationInvoiceDataIssuerTypeSelf),
      },
    },
  },
}
result, err := sc.V1PaymentLinks.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new PaymentLinkCreateOptions
{
    LineItems = new List<PaymentLinkLineItemOptions>
    {
        new PaymentLinkLineItemOptions { Price = "{{PRICE_ID}}", Quantity = 2 },
    },
    AutomaticTax = new PaymentLinkAutomaticTaxOptions
    {
        Enabled = true,
        Liability = new PaymentLinkAutomaticTaxLiabilityOptions { Type = "self" },
    },
    TransferData = new PaymentLinkTransferDataOptions
    {
        Destination = "{{CONNECTEDACCOUNT_ID}}",
    },
    InvoiceCreation = new PaymentLinkInvoiceCreationOptions
    {
        Enabled = true,
        InvoiceData = new PaymentLinkInvoiceCreationInvoiceDataOptions
        {
            Issuer = new PaymentLinkInvoiceCreationInvoiceDataIssuerOptions
            {
                Type = "self",
            },
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.PaymentLinks;
PaymentLink paymentLink = service.Create(options);
```

#### Separate charges and transfers

For the Payment Links API calls:

- Include [automatic_tax[liability]](https://docs.stripe.com/api/payment_links/payment_links/create.md#create_payment_link-automatic_tax-liability) with `type=self`.
- (Optional) If you’re [automatically sending invoices](https://docs.stripe.com/payment-links/post-payment.md#automatically-send-paid-invoices), include [invoice_creation[invoice_data][issuer]](https://docs.stripe.com/api/payment_links/payment_links/create.md#create_payment_link-invoice_creation-invoice_data-issuer) with `type=self`.
- (Optional) If the connected account is the [settlement merchant](https://docs.stripe.com/connect/separate-charges-and-transfers.md#settlement-merchant), include [on_behalf_of](https://docs.stripe.com/api/payment_links/payment_links/create.md#create_payment_link-on_behalf_of) with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/payment_links \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=2 \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=self \
  -d "invoice_creation[enabled]"=true \
  -d "invoice_creation[invoice_data][issuer][type]"=self
```

```cli
stripe payment_links create  \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=2 \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=self \
  -d "invoice_creation[enabled]"=true \
  -d "invoice_creation[invoice_data][issuer][type]"=self
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

payment_link = client.v1.payment_links.create({
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 2,
    },
  ],
  automatic_tax: {
    enabled: true,
    liability: {type: 'self'},
  },
  invoice_creation: {
    enabled: true,
    invoice_data: {issuer: {type: 'self'}},
  },
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
payment_link = client.v1.payment_links.create({
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 2}],
  "automatic_tax": {"enabled": True, "liability": {"type": "self"}},
  "invoice_creation": {"enabled": True, "invoice_data": {"issuer": {"type": "self"}}},
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$paymentLink = $stripe->paymentLinks->create([
  'line_items' => [
    [
      'price' => '{{PRICE_ID}}',
      'quantity' => 2,
    ],
  ],
  'automatic_tax' => [
    'enabled' => true,
    'liability' => ['type' => 'self'],
  ],
  'invoice_creation' => [
    'enabled' => true,
    'invoice_data' => ['issuer' => ['type' => 'self']],
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

PaymentLinkCreateParams params =
  PaymentLinkCreateParams.builder()
    .addLineItem(
      PaymentLinkCreateParams.LineItem.builder()
        .setPrice("{{PRICE_ID}}")
        .setQuantity(2L)
        .build()
    )
    .setAutomaticTax(
      PaymentLinkCreateParams.AutomaticTax.builder()
        .setEnabled(true)
        .setLiability(
          PaymentLinkCreateParams.AutomaticTax.Liability.builder()
            .setType(PaymentLinkCreateParams.AutomaticTax.Liability.Type.SELF)
            .build()
        )
        .build()
    )
    .setInvoiceCreation(
      PaymentLinkCreateParams.InvoiceCreation.builder()
        .setEnabled(true)
        .setInvoiceData(
          PaymentLinkCreateParams.InvoiceCreation.InvoiceData.builder()
            .setIssuer(
              PaymentLinkCreateParams.InvoiceCreation.InvoiceData.Issuer.builder()
                .setType(
                  PaymentLinkCreateParams.InvoiceCreation.InvoiceData.Issuer.Type.SELF
                )
                .build()
            )
            .build()
        )
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
PaymentLink paymentLink = client.v1().paymentLinks().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const paymentLink = await stripe.paymentLinks.create({
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 2,
    },
  ],
  automatic_tax: {
    enabled: true,
    liability: {
      type: 'self',
    },
  },
  invoice_creation: {
    enabled: true,
    invoice_data: {
      issuer: {
        type: 'self',
      },
    },
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.PaymentLinkCreateParams{
  LineItems: []*stripe.PaymentLinkCreateLineItemParams{
    &stripe.PaymentLinkCreateLineItemParams{
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(2),
    },
  },
  AutomaticTax: &stripe.PaymentLinkCreateAutomaticTaxParams{
    Enabled: stripe.Bool(true),
    Liability: &stripe.PaymentLinkCreateAutomaticTaxLiabilityParams{
      Type: stripe.String(stripe.PaymentLinkAutomaticTaxLiabilityTypeSelf),
    },
  },
  InvoiceCreation: &stripe.PaymentLinkCreateInvoiceCreationParams{
    Enabled: stripe.Bool(true),
    InvoiceData: &stripe.PaymentLinkCreateInvoiceCreationInvoiceDataParams{
      Issuer: &stripe.PaymentLinkCreateInvoiceCreationInvoiceDataIssuerParams{
        Type: stripe.String(stripe.PaymentLinkInvoiceCreationInvoiceDataIssuerTypeSelf),
      },
    },
  },
}
result, err := sc.V1PaymentLinks.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new PaymentLinkCreateOptions
{
    LineItems = new List<PaymentLinkLineItemOptions>
    {
        new PaymentLinkLineItemOptions { Price = "{{PRICE_ID}}", Quantity = 2 },
    },
    AutomaticTax = new PaymentLinkAutomaticTaxOptions
    {
        Enabled = true,
        Liability = new PaymentLinkAutomaticTaxLiabilityOptions { Type = "self" },
    },
    InvoiceCreation = new PaymentLinkInvoiceCreationOptions
    {
        Enabled = true,
        InvoiceData = new PaymentLinkInvoiceCreationInvoiceDataOptions
        {
            Issuer = new PaymentLinkInvoiceCreationInvoiceDataIssuerOptions
            {
                Type = "self",
            },
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.PaymentLinks;
PaymentLink paymentLink = service.Create(options);
```

For the Transfers API calls:

- Include [source_transaction](https://docs.stripe.com/api/transfers/create.md#create_transfer-source_transaction) to tie the transfer to the PaymentIntent created by the Payment Link.
- Include [destination](https://docs.stripe.com/api/transfers/create.md#create_transfer-destination) with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/transfers \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d amount=1000 \
  -d currency=usd \
  -d source_transaction="{{CHARGE_ID}}" \
  -d destination="{{CONNECTEDACCOUNT_ID}}"
```

```cli
stripe transfers create  \
  --amount=1000 \
  --currency=usd \
  --source-transaction="{{CHARGE_ID}}" \
  --destination="{{CONNECTEDACCOUNT_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

transfer = client.v1.transfers.create({
  amount: 1000,
  currency: 'usd',
  source_transaction: '{{CHARGE_ID}}',
  destination: '{{CONNECTEDACCOUNT_ID}}',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
transfer = client.v1.transfers.create({
  "amount": 1000,
  "currency": "usd",
  "source_transaction": "{{CHARGE_ID}}",
  "destination": "{{CONNECTEDACCOUNT_ID}}",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$transfer = $stripe->transfers->create([
  'amount' => 1000,
  'currency' => 'usd',
  'source_transaction' => '{{CHARGE_ID}}',
  'destination' => '{{CONNECTEDACCOUNT_ID}}',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

TransferCreateParams params =
  TransferCreateParams.builder()
    .setAmount(1000L)
    .setCurrency("usd")
    .setSourceTransaction("{{CHARGE_ID}}")
    .setDestination("{{CONNECTEDACCOUNT_ID}}")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Transfer transfer = client.v1().transfers().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const transfer = await stripe.transfers.create({
  amount: 1000,
  currency: 'usd',
  source_transaction: '{{CHARGE_ID}}',
  destination: '{{CONNECTEDACCOUNT_ID}}',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TransferCreateParams{
  Amount: stripe.Int64(1000),
  Currency: stripe.String(stripe.CurrencyUSD),
  SourceTransaction: stripe.String("{{CHARGE_ID}}"),
  Destination: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
}
result, err := sc.V1Transfers.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new TransferCreateOptions
{
    Amount = 1000,
    Currency = "usd",
    SourceTransaction = "{{CHARGE_ID}}",
    Destination = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Transfers;
Transfer transfer = service.Create(options);
```

### Payment Links for subscriptions

Pick one of the currently supported charge types that allow your platform account to be *liable for tax* (The responsibility for collecting and reporting taxes for transactions in a Connect integration. It can belong to the platform or to connected accounts, depending on your business model, government regulations, and individual transaction details) with [Stripe Payment Links](https://docs.stripe.com/tax/payment-links.md):

#### Direct charges

This charge type isn’t supported for use cases involving a connected account where the platform is *liable for tax* (The responsibility for collecting and reporting taxes for transactions in a Connect integration. It can belong to the platform or to connected accounts, depending on your business model, government regulations, and individual transaction details).

#### Destination charges

For the Payment Links API calls:

- Include [automatic_tax[liability]](https://docs.stripe.com/api/payment_links/payment_links/create.md#create_payment_link-automatic_tax-liability) with `type=self`.
- Include [transfer_data[destination]](https://docs.stripe.com/api/payment_links/payment_links/create.md#create_payment_link-transfer_data) with the value of the connected account ID.
- Include [subscription_data[invoice_settings][issuer]](https://docs.stripe.com/api/payment_links/payment_links/create.md#create_payment_link-subscription_data-invoice_settings-issuer) with `type=self`.
- (Optional) If the connected account is the [settlement merchant](https://docs.stripe.com/connect/destination-charges.md#settlement-merchant), include [subscription_data[on_behalf_of]](https://docs.stripe.com/api/payment_links/payment_links/create.md#create_payment_link-on_behalf_of) with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/payment_links \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=self \
  -d "transfer_data[destination]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "subscription_data[invoice_settings][issuer][type]"=self
```

```cli
stripe payment_links create  \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=self \
  -d "transfer_data[destination]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "subscription_data[invoice_settings][issuer][type]"=self
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

payment_link = client.v1.payment_links.create({
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  automatic_tax: {
    enabled: true,
    liability: {type: 'self'},
  },
  transfer_data: {destination: '{{CONNECTEDACCOUNT_ID}}'},
  subscription_data: {invoice_settings: {issuer: {type: 'self'}}},
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
payment_link = client.v1.payment_links.create({
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 1}],
  "automatic_tax": {"enabled": True, "liability": {"type": "self"}},
  "transfer_data": {"destination": "{{CONNECTEDACCOUNT_ID}}"},
  "subscription_data": {"invoice_settings": {"issuer": {"type": "self"}}},
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$paymentLink = $stripe->paymentLinks->create([
  'line_items' => [
    [
      'price' => '{{PRICE_ID}}',
      'quantity' => 1,
    ],
  ],
  'automatic_tax' => [
    'enabled' => true,
    'liability' => ['type' => 'self'],
  ],
  'transfer_data' => ['destination' => '{{CONNECTEDACCOUNT_ID}}'],
  'subscription_data' => ['invoice_settings' => ['issuer' => ['type' => 'self']]],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

PaymentLinkCreateParams params =
  PaymentLinkCreateParams.builder()
    .addLineItem(
      PaymentLinkCreateParams.LineItem.builder()
        .setPrice("{{PRICE_ID}}")
        .setQuantity(1L)
        .build()
    )
    .setAutomaticTax(
      PaymentLinkCreateParams.AutomaticTax.builder()
        .setEnabled(true)
        .setLiability(
          PaymentLinkCreateParams.AutomaticTax.Liability.builder()
            .setType(PaymentLinkCreateParams.AutomaticTax.Liability.Type.SELF)
            .build()
        )
        .build()
    )
    .setTransferData(
      PaymentLinkCreateParams.TransferData.builder()
        .setDestination("{{CONNECTEDACCOUNT_ID}}")
        .build()
    )
    .setSubscriptionData(
      PaymentLinkCreateParams.SubscriptionData.builder()
        .setInvoiceSettings(
          PaymentLinkCreateParams.SubscriptionData.InvoiceSettings.builder()
            .setIssuer(
              PaymentLinkCreateParams.SubscriptionData.InvoiceSettings.Issuer.builder()
                .setType(
                  PaymentLinkCreateParams.SubscriptionData.InvoiceSettings.Issuer.Type.SELF
                )
                .build()
            )
            .build()
        )
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
PaymentLink paymentLink = client.v1().paymentLinks().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const paymentLink = await stripe.paymentLinks.create({
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  automatic_tax: {
    enabled: true,
    liability: {
      type: 'self',
    },
  },
  transfer_data: {
    destination: '{{CONNECTEDACCOUNT_ID}}',
  },
  subscription_data: {
    invoice_settings: {
      issuer: {
        type: 'self',
      },
    },
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.PaymentLinkCreateParams{
  LineItems: []*stripe.PaymentLinkCreateLineItemParams{
    &stripe.PaymentLinkCreateLineItemParams{
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(1),
    },
  },
  AutomaticTax: &stripe.PaymentLinkCreateAutomaticTaxParams{
    Enabled: stripe.Bool(true),
    Liability: &stripe.PaymentLinkCreateAutomaticTaxLiabilityParams{
      Type: stripe.String(stripe.PaymentLinkAutomaticTaxLiabilityTypeSelf),
    },
  },
  TransferData: &stripe.PaymentLinkCreateTransferDataParams{
    Destination: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
  },
  SubscriptionData: &stripe.PaymentLinkCreateSubscriptionDataParams{
    InvoiceSettings: &stripe.PaymentLinkCreateSubscriptionDataInvoiceSettingsParams{
      Issuer: &stripe.PaymentLinkCreateSubscriptionDataInvoiceSettingsIssuerParams{
        Type: stripe.String(stripe.PaymentLinkSubscriptionDataInvoiceSettingsIssuerTypeSelf),
      },
    },
  },
}
result, err := sc.V1PaymentLinks.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new PaymentLinkCreateOptions
{
    LineItems = new List<PaymentLinkLineItemOptions>
    {
        new PaymentLinkLineItemOptions { Price = "{{PRICE_ID}}", Quantity = 1 },
    },
    AutomaticTax = new PaymentLinkAutomaticTaxOptions
    {
        Enabled = true,
        Liability = new PaymentLinkAutomaticTaxLiabilityOptions { Type = "self" },
    },
    TransferData = new PaymentLinkTransferDataOptions
    {
        Destination = "{{CONNECTEDACCOUNT_ID}}",
    },
    SubscriptionData = new PaymentLinkSubscriptionDataOptions
    {
        InvoiceSettings = new PaymentLinkSubscriptionDataInvoiceSettingsOptions
        {
            Issuer = new PaymentLinkSubscriptionDataInvoiceSettingsIssuerOptions
            {
                Type = "self",
            },
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.PaymentLinks;
PaymentLink paymentLink = service.Create(options);
```

#### Separate charges and transfers

For the Payment Links API calls:

- Include [automatic_tax[liability]](https://docs.stripe.com/api/payment_links/payment_links/create.md#create_payment_link-automatic_tax-liability) with `type=self`.
- Include [subscription_data[invoice_settings][issuer]](https://docs.stripe.com/api/payment_links/payment_links/create.md#create_payment_link-subscription_data-invoice_settings-issuer) with `type=self`.
- (Optional) If the connected account is the [settlement merchant](https://docs.stripe.com/connect/separate-charges-and-transfers.md#settlement-merchant), include [subscription_data[on_behalf_of]](https://docs.stripe.com/api/payment_links/payment_links/create.md#create_payment_link-on_behalf_of) with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/payment_links \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=self \
  -d "subscription_data[invoice_settings][issuer][type]"=self
```

```cli
stripe payment_links create  \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=self \
  -d "subscription_data[invoice_settings][issuer][type]"=self
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

payment_link = client.v1.payment_links.create({
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  automatic_tax: {
    enabled: true,
    liability: {type: 'self'},
  },
  subscription_data: {invoice_settings: {issuer: {type: 'self'}}},
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
payment_link = client.v1.payment_links.create({
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 1}],
  "automatic_tax": {"enabled": True, "liability": {"type": "self"}},
  "subscription_data": {"invoice_settings": {"issuer": {"type": "self"}}},
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$paymentLink = $stripe->paymentLinks->create([
  'line_items' => [
    [
      'price' => '{{PRICE_ID}}',
      'quantity' => 1,
    ],
  ],
  'automatic_tax' => [
    'enabled' => true,
    'liability' => ['type' => 'self'],
  ],
  'subscription_data' => ['invoice_settings' => ['issuer' => ['type' => 'self']]],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

PaymentLinkCreateParams params =
  PaymentLinkCreateParams.builder()
    .addLineItem(
      PaymentLinkCreateParams.LineItem.builder()
        .setPrice("{{PRICE_ID}}")
        .setQuantity(1L)
        .build()
    )
    .setAutomaticTax(
      PaymentLinkCreateParams.AutomaticTax.builder()
        .setEnabled(true)
        .setLiability(
          PaymentLinkCreateParams.AutomaticTax.Liability.builder()
            .setType(PaymentLinkCreateParams.AutomaticTax.Liability.Type.SELF)
            .build()
        )
        .build()
    )
    .setSubscriptionData(
      PaymentLinkCreateParams.SubscriptionData.builder()
        .setInvoiceSettings(
          PaymentLinkCreateParams.SubscriptionData.InvoiceSettings.builder()
            .setIssuer(
              PaymentLinkCreateParams.SubscriptionData.InvoiceSettings.Issuer.builder()
                .setType(
                  PaymentLinkCreateParams.SubscriptionData.InvoiceSettings.Issuer.Type.SELF
                )
                .build()
            )
            .build()
        )
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
PaymentLink paymentLink = client.v1().paymentLinks().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const paymentLink = await stripe.paymentLinks.create({
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  automatic_tax: {
    enabled: true,
    liability: {
      type: 'self',
    },
  },
  subscription_data: {
    invoice_settings: {
      issuer: {
        type: 'self',
      },
    },
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.PaymentLinkCreateParams{
  LineItems: []*stripe.PaymentLinkCreateLineItemParams{
    &stripe.PaymentLinkCreateLineItemParams{
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(1),
    },
  },
  AutomaticTax: &stripe.PaymentLinkCreateAutomaticTaxParams{
    Enabled: stripe.Bool(true),
    Liability: &stripe.PaymentLinkCreateAutomaticTaxLiabilityParams{
      Type: stripe.String(stripe.PaymentLinkAutomaticTaxLiabilityTypeSelf),
    },
  },
  SubscriptionData: &stripe.PaymentLinkCreateSubscriptionDataParams{
    InvoiceSettings: &stripe.PaymentLinkCreateSubscriptionDataInvoiceSettingsParams{
      Issuer: &stripe.PaymentLinkCreateSubscriptionDataInvoiceSettingsIssuerParams{
        Type: stripe.String(stripe.PaymentLinkSubscriptionDataInvoiceSettingsIssuerTypeSelf),
      },
    },
  },
}
result, err := sc.V1PaymentLinks.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new PaymentLinkCreateOptions
{
    LineItems = new List<PaymentLinkLineItemOptions>
    {
        new PaymentLinkLineItemOptions { Price = "{{PRICE_ID}}", Quantity = 1 },
    },
    AutomaticTax = new PaymentLinkAutomaticTaxOptions
    {
        Enabled = true,
        Liability = new PaymentLinkAutomaticTaxLiabilityOptions { Type = "self" },
    },
    SubscriptionData = new PaymentLinkSubscriptionDataOptions
    {
        InvoiceSettings = new PaymentLinkSubscriptionDataInvoiceSettingsOptions
        {
            Issuer = new PaymentLinkSubscriptionDataInvoiceSettingsIssuerOptions
            {
                Type = "self",
            },
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.PaymentLinks;
PaymentLink paymentLink = service.Create(options);
```

For the Transfers API calls:

- Include [source_transaction](https://docs.stripe.com/api/transfers/create.md#create_transfer-source_transaction) to tie the transfer to the PaymentIntent created by the Payment Link.
- Include [destination](https://docs.stripe.com/api/transfers/create.md#create_transfer-destination) with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/transfers \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d amount=1000 \
  -d currency=usd \
  -d source_transaction="{{CHARGE_ID}}" \
  -d destination="{{CONNECTEDACCOUNT_ID}}"
```

```cli
stripe transfers create  \
  --amount=1000 \
  --currency=usd \
  --source-transaction="{{CHARGE_ID}}" \
  --destination="{{CONNECTEDACCOUNT_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

transfer = client.v1.transfers.create({
  amount: 1000,
  currency: 'usd',
  source_transaction: '{{CHARGE_ID}}',
  destination: '{{CONNECTEDACCOUNT_ID}}',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
transfer = client.v1.transfers.create({
  "amount": 1000,
  "currency": "usd",
  "source_transaction": "{{CHARGE_ID}}",
  "destination": "{{CONNECTEDACCOUNT_ID}}",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$transfer = $stripe->transfers->create([
  'amount' => 1000,
  'currency' => 'usd',
  'source_transaction' => '{{CHARGE_ID}}',
  'destination' => '{{CONNECTEDACCOUNT_ID}}',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

TransferCreateParams params =
  TransferCreateParams.builder()
    .setAmount(1000L)
    .setCurrency("usd")
    .setSourceTransaction("{{CHARGE_ID}}")
    .setDestination("{{CONNECTEDACCOUNT_ID}}")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Transfer transfer = client.v1().transfers().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const transfer = await stripe.transfers.create({
  amount: 1000,
  currency: 'usd',
  source_transaction: '{{CHARGE_ID}}',
  destination: '{{CONNECTEDACCOUNT_ID}}',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TransferCreateParams{
  Amount: stripe.Int64(1000),
  Currency: stripe.String(stripe.CurrencyUSD),
  SourceTransaction: stripe.String("{{CHARGE_ID}}"),
  Destination: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
}
result, err := sc.V1Transfers.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new TransferCreateOptions
{
    Amount = 1000,
    Currency = "usd",
    SourceTransaction = "{{CHARGE_ID}}",
    Destination = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Transfers;
Transfer transfer = service.Create(options);
```

### Checkout

### Checkout Sessions for one-time payments

Pick one of the currently supported charge types that allow your platform account to be *liable for tax* (The responsibility for collecting and reporting taxes for transactions in a Connect integration. It can belong to the platform or to connected accounts, depending on your business model, government regulations, and individual transaction details) with [Stripe Checkout](https://docs.stripe.com/tax/checkout.md):

#### Direct charges

This charge type isn’t supported for use cases involving a connected account where the platform is *liable for tax* (The responsibility for collecting and reporting taxes for transactions in a Connect integration. It can belong to the platform or to connected accounts, depending on your business model, government regulations, and individual transaction details).

#### Destination charges

For the Checkout Sessions API calls:

- Include [automatic_tax[liability]](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-automatic_tax-liability) with `type=self`.
- Include [payment_intent_data[transfer_data][destination]](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-payment_intent_data-transfer_data-destination) with the value of the connected account ID.
- (Optional) If you’re [automatically sending invoices](https://docs.stripe.com/payments/checkout/receipts.md?payment-ui=stripe-hosted#automatically-send-receipts), include [invoice_creation[invoice_data][issuer]](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-invoice_creation-invoice_data-issuer) with `type=self`.
- (Optional) If the connected account is the [settlement merchant](https://docs.stripe.com/connect/destination-charges.md#settlement-merchant), include [payment_intent_data[on_behalf_of]](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-payment_intent_data-on_behalf_of) with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=2 \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=self \
  -d "payment_intent_data[transfer_data][destination]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "invoice_creation[enabled]"=true \
  -d "invoice_creation[invoice_data][issuer][type]"=self \
  -d mode=payment \
  --data-urlencode success_url="https://example.com/success"
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=2 \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=self \
  -d "payment_intent_data[transfer_data][destination]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "invoice_creation[enabled]"=true \
  -d "invoice_creation[invoice_data][issuer][type]"=self \
  --mode=payment \
  --success-url="https://example.com/success"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 2,
    },
  ],
  automatic_tax: {
    enabled: true,
    liability: {type: 'self'},
  },
  payment_intent_data: {transfer_data: {destination: '{{CONNECTEDACCOUNT_ID}}'}},
  invoice_creation: {
    enabled: true,
    invoice_data: {issuer: {type: 'self'}},
  },
  mode: 'payment',
  success_url: 'https://example.com/success',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 2}],
  "automatic_tax": {"enabled": True, "liability": {"type": "self"}},
  "payment_intent_data": {
    "transfer_data": {"destination": "{{CONNECTEDACCOUNT_ID}}"},
  },
  "invoice_creation": {"enabled": True, "invoice_data": {"issuer": {"type": "self"}}},
  "mode": "payment",
  "success_url": "https://example.com/success",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'line_items' => [
    [
      'price' => '{{PRICE_ID}}',
      'quantity' => 2,
    ],
  ],
  'automatic_tax' => [
    'enabled' => true,
    'liability' => ['type' => 'self'],
  ],
  'payment_intent_data' => [
    'transfer_data' => ['destination' => '{{CONNECTEDACCOUNT_ID}}'],
  ],
  'invoice_creation' => [
    'enabled' => true,
    'invoice_data' => ['issuer' => ['type' => 'self']],
  ],
  'mode' => 'payment',
  'success_url' => 'https://example.com/success',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPrice("{{PRICE_ID}}")
        .setQuantity(2L)
        .build()
    )
    .setAutomaticTax(
      SessionCreateParams.AutomaticTax.builder()
        .setEnabled(true)
        .setLiability(
          SessionCreateParams.AutomaticTax.Liability.builder()
            .setType(SessionCreateParams.AutomaticTax.Liability.Type.SELF)
            .build()
        )
        .build()
    )
    .setPaymentIntentData(
      SessionCreateParams.PaymentIntentData.builder()
        .setTransferData(
          SessionCreateParams.PaymentIntentData.TransferData.builder()
            .setDestination("{{CONNECTEDACCOUNT_ID}}")
            .build()
        )
        .build()
    )
    .setInvoiceCreation(
      SessionCreateParams.InvoiceCreation.builder()
        .setEnabled(true)
        .setInvoiceData(
          SessionCreateParams.InvoiceCreation.InvoiceData.builder()
            .setIssuer(
              SessionCreateParams.InvoiceCreation.InvoiceData.Issuer.builder()
                .setType(SessionCreateParams.InvoiceCreation.InvoiceData.Issuer.Type.SELF)
                .build()
            )
            .build()
        )
        .build()
    )
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setSuccessUrl("https://example.com/success")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session = client.v1().checkout().sessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 2,
    },
  ],
  automatic_tax: {
    enabled: true,
    liability: {
      type: 'self',
    },
  },
  payment_intent_data: {
    transfer_data: {
      destination: '{{CONNECTEDACCOUNT_ID}}',
    },
  },
  invoice_creation: {
    enabled: true,
    invoice_data: {
      issuer: {
        type: 'self',
      },
    },
  },
  mode: 'payment',
  success_url: 'https://example.com/success',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(2),
    },
  },
  AutomaticTax: &stripe.CheckoutSessionCreateAutomaticTaxParams{
    Enabled: stripe.Bool(true),
    Liability: &stripe.CheckoutSessionCreateAutomaticTaxLiabilityParams{
      Type: stripe.String(stripe.CheckoutSessionAutomaticTaxLiabilityTypeSelf),
    },
  },
  PaymentIntentData: &stripe.CheckoutSessionCreatePaymentIntentDataParams{
    TransferData: &stripe.CheckoutSessionCreatePaymentIntentDataTransferDataParams{
      Destination: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
    },
  },
  InvoiceCreation: &stripe.CheckoutSessionCreateInvoiceCreationParams{
    Enabled: stripe.Bool(true),
    InvoiceData: &stripe.CheckoutSessionCreateInvoiceCreationInvoiceDataParams{
      Issuer: &stripe.CheckoutSessionCreateInvoiceCreationInvoiceDataIssuerParams{
        Type: stripe.String(stripe.CheckoutSessionInvoiceCreationInvoiceDataIssuerTypeSelf),
      },
    },
  },
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  SuccessURL: stripe.String("https://example.com/success"),
}
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionCreateOptions
{
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            Price = "{{PRICE_ID}}",
            Quantity = 2,
        },
    },
    AutomaticTax = new Stripe.Checkout.SessionAutomaticTaxOptions
    {
        Enabled = true,
        Liability = new Stripe.Checkout.SessionAutomaticTaxLiabilityOptions
        {
            Type = "self",
        },
    },
    PaymentIntentData = new Stripe.Checkout.SessionPaymentIntentDataOptions
    {
        TransferData = new Stripe.Checkout.SessionPaymentIntentDataTransferDataOptions
        {
            Destination = "{{CONNECTEDACCOUNT_ID}}",
        },
    },
    InvoiceCreation = new Stripe.Checkout.SessionInvoiceCreationOptions
    {
        Enabled = true,
        InvoiceData = new Stripe.Checkout.SessionInvoiceCreationInvoiceDataOptions
        {
            Issuer = new Stripe.Checkout.SessionInvoiceCreationInvoiceDataIssuerOptions
            {
                Type = "self",
            },
        },
    },
    Mode = "payment",
    SuccessUrl = "https://example.com/success",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

#### Separate charges and transfers

For the Checkout Sessions API calls:

- Include [automatic_tax[liability]](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-automatic_tax-liability) with `type=self`.
- (Optional) If you’re [automatically sending invoices](https://docs.stripe.com/payments/checkout/receipts.md?payment-ui=stripe-hosted#automatically-send-receipts), include [invoice_creation[invoice_data][issuer]](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-invoice_creation-invoice_data-issuer) with `type=self`.
- (Optional) If the connected account is the [settlement merchant](https://docs.stripe.com/connect/separate-charges-and-transfers.md#settlement-merchant), include [payment_intent_data[on_behalf_of]](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-payment_intent_data-on_behalf_of) with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=2 \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=self \
  -d "invoice_creation[enabled]"=true \
  -d "invoice_creation[invoice_data][issuer][type]"=self \
  -d mode=payment \
  --data-urlencode success_url="https://example.com/success"
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=2 \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=self \
  -d "invoice_creation[enabled]"=true \
  -d "invoice_creation[invoice_data][issuer][type]"=self \
  --mode=payment \
  --success-url="https://example.com/success"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 2,
    },
  ],
  automatic_tax: {
    enabled: true,
    liability: {type: 'self'},
  },
  invoice_creation: {
    enabled: true,
    invoice_data: {issuer: {type: 'self'}},
  },
  mode: 'payment',
  success_url: 'https://example.com/success',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 2}],
  "automatic_tax": {"enabled": True, "liability": {"type": "self"}},
  "invoice_creation": {"enabled": True, "invoice_data": {"issuer": {"type": "self"}}},
  "mode": "payment",
  "success_url": "https://example.com/success",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'line_items' => [
    [
      'price' => '{{PRICE_ID}}',
      'quantity' => 2,
    ],
  ],
  'automatic_tax' => [
    'enabled' => true,
    'liability' => ['type' => 'self'],
  ],
  'invoice_creation' => [
    'enabled' => true,
    'invoice_data' => ['issuer' => ['type' => 'self']],
  ],
  'mode' => 'payment',
  'success_url' => 'https://example.com/success',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPrice("{{PRICE_ID}}")
        .setQuantity(2L)
        .build()
    )
    .setAutomaticTax(
      SessionCreateParams.AutomaticTax.builder()
        .setEnabled(true)
        .setLiability(
          SessionCreateParams.AutomaticTax.Liability.builder()
            .setType(SessionCreateParams.AutomaticTax.Liability.Type.SELF)
            .build()
        )
        .build()
    )
    .setInvoiceCreation(
      SessionCreateParams.InvoiceCreation.builder()
        .setEnabled(true)
        .setInvoiceData(
          SessionCreateParams.InvoiceCreation.InvoiceData.builder()
            .setIssuer(
              SessionCreateParams.InvoiceCreation.InvoiceData.Issuer.builder()
                .setType(SessionCreateParams.InvoiceCreation.InvoiceData.Issuer.Type.SELF)
                .build()
            )
            .build()
        )
        .build()
    )
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setSuccessUrl("https://example.com/success")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session = client.v1().checkout().sessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 2,
    },
  ],
  automatic_tax: {
    enabled: true,
    liability: {
      type: 'self',
    },
  },
  invoice_creation: {
    enabled: true,
    invoice_data: {
      issuer: {
        type: 'self',
      },
    },
  },
  mode: 'payment',
  success_url: 'https://example.com/success',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(2),
    },
  },
  AutomaticTax: &stripe.CheckoutSessionCreateAutomaticTaxParams{
    Enabled: stripe.Bool(true),
    Liability: &stripe.CheckoutSessionCreateAutomaticTaxLiabilityParams{
      Type: stripe.String(stripe.CheckoutSessionAutomaticTaxLiabilityTypeSelf),
    },
  },
  InvoiceCreation: &stripe.CheckoutSessionCreateInvoiceCreationParams{
    Enabled: stripe.Bool(true),
    InvoiceData: &stripe.CheckoutSessionCreateInvoiceCreationInvoiceDataParams{
      Issuer: &stripe.CheckoutSessionCreateInvoiceCreationInvoiceDataIssuerParams{
        Type: stripe.String(stripe.CheckoutSessionInvoiceCreationInvoiceDataIssuerTypeSelf),
      },
    },
  },
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  SuccessURL: stripe.String("https://example.com/success"),
}
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionCreateOptions
{
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            Price = "{{PRICE_ID}}",
            Quantity = 2,
        },
    },
    AutomaticTax = new Stripe.Checkout.SessionAutomaticTaxOptions
    {
        Enabled = true,
        Liability = new Stripe.Checkout.SessionAutomaticTaxLiabilityOptions
        {
            Type = "self",
        },
    },
    InvoiceCreation = new Stripe.Checkout.SessionInvoiceCreationOptions
    {
        Enabled = true,
        InvoiceData = new Stripe.Checkout.SessionInvoiceCreationInvoiceDataOptions
        {
            Issuer = new Stripe.Checkout.SessionInvoiceCreationInvoiceDataIssuerOptions
            {
                Type = "self",
            },
        },
    },
    Mode = "payment",
    SuccessUrl = "https://example.com/success",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

For the Transfers API calls:

- Include [source_transaction](https://docs.stripe.com/api/transfers/create.md#create_transfer-source_transaction) to tie the transfer to the PaymentIntent created by the Checkout Session.
- Include [destination](https://docs.stripe.com/api/transfers/create.md#create_transfer-destination) with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/transfers \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d amount=1000 \
  -d currency=usd \
  -d source_transaction="{{CHARGE_ID}}" \
  -d destination="{{CONNECTEDACCOUNT_ID}}"
```

```cli
stripe transfers create  \
  --amount=1000 \
  --currency=usd \
  --source-transaction="{{CHARGE_ID}}" \
  --destination="{{CONNECTEDACCOUNT_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

transfer = client.v1.transfers.create({
  amount: 1000,
  currency: 'usd',
  source_transaction: '{{CHARGE_ID}}',
  destination: '{{CONNECTEDACCOUNT_ID}}',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
transfer = client.v1.transfers.create({
  "amount": 1000,
  "currency": "usd",
  "source_transaction": "{{CHARGE_ID}}",
  "destination": "{{CONNECTEDACCOUNT_ID}}",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$transfer = $stripe->transfers->create([
  'amount' => 1000,
  'currency' => 'usd',
  'source_transaction' => '{{CHARGE_ID}}',
  'destination' => '{{CONNECTEDACCOUNT_ID}}',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

TransferCreateParams params =
  TransferCreateParams.builder()
    .setAmount(1000L)
    .setCurrency("usd")
    .setSourceTransaction("{{CHARGE_ID}}")
    .setDestination("{{CONNECTEDACCOUNT_ID}}")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Transfer transfer = client.v1().transfers().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const transfer = await stripe.transfers.create({
  amount: 1000,
  currency: 'usd',
  source_transaction: '{{CHARGE_ID}}',
  destination: '{{CONNECTEDACCOUNT_ID}}',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TransferCreateParams{
  Amount: stripe.Int64(1000),
  Currency: stripe.String(stripe.CurrencyUSD),
  SourceTransaction: stripe.String("{{CHARGE_ID}}"),
  Destination: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
}
result, err := sc.V1Transfers.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new TransferCreateOptions
{
    Amount = 1000,
    Currency = "usd",
    SourceTransaction = "{{CHARGE_ID}}",
    Destination = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Transfers;
Transfer transfer = service.Create(options);
```

### Checkout Sessions for subscriptions

Pick one of the currently supported charge types that allow your platform account to be *liable for tax* (The responsibility for collecting and reporting taxes for transactions in a Connect integration. It can belong to the platform or to connected accounts, depending on your business model, government regulations, and individual transaction details) with [Stripe Checkout](https://docs.stripe.com/tax/checkout.md):

#### Direct charges

This charge type isn’t supported for use cases involving a connected account where the platform is *liable for tax* (The responsibility for collecting and reporting taxes for transactions in a Connect integration. It can belong to the platform or to connected accounts, depending on your business model, government regulations, and individual transaction details).

#### Destination charges

For the Checkout Sessions API calls:

- Include [automatic_tax[liability]](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-automatic_tax-liability) with `type=self`.
- Include [subscription_data[transfer_data][destination]](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-subscription_data-transfer_data-destination) with the value of the connected account ID.
- Include [subscription_data[invoice_settings][issuer]](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-subscription_data-invoice_settings-issuer) with `type=self`.
- (Optional) If the connected account is the [settlement merchant](https://docs.stripe.com/connect/destination-charges.md#settlement-merchant), include [subscription_data[on_behalf_of]](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-subscription_data-on_behalf_of) with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=self \
  -d "subscription_data[transfer_data][destination]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "subscription_data[invoice_settings][issuer][type]"=self \
  -d mode=subscription \
  --data-urlencode success_url="https://example.com/success"
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=self \
  -d "subscription_data[transfer_data][destination]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "subscription_data[invoice_settings][issuer][type]"=self \
  --mode=subscription \
  --success-url="https://example.com/success"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  automatic_tax: {
    enabled: true,
    liability: {type: 'self'},
  },
  subscription_data: {
    transfer_data: {destination: '{{CONNECTEDACCOUNT_ID}}'},
    invoice_settings: {issuer: {type: 'self'}},
  },
  mode: 'subscription',
  success_url: 'https://example.com/success',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 1}],
  "automatic_tax": {"enabled": True, "liability": {"type": "self"}},
  "subscription_data": {
    "transfer_data": {"destination": "{{CONNECTEDACCOUNT_ID}}"},
    "invoice_settings": {"issuer": {"type": "self"}},
  },
  "mode": "subscription",
  "success_url": "https://example.com/success",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'line_items' => [
    [
      'price' => '{{PRICE_ID}}',
      'quantity' => 1,
    ],
  ],
  'automatic_tax' => [
    'enabled' => true,
    'liability' => ['type' => 'self'],
  ],
  'subscription_data' => [
    'transfer_data' => ['destination' => '{{CONNECTEDACCOUNT_ID}}'],
    'invoice_settings' => ['issuer' => ['type' => 'self']],
  ],
  'mode' => 'subscription',
  'success_url' => 'https://example.com/success',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPrice("{{PRICE_ID}}")
        .setQuantity(1L)
        .build()
    )
    .setAutomaticTax(
      SessionCreateParams.AutomaticTax.builder()
        .setEnabled(true)
        .setLiability(
          SessionCreateParams.AutomaticTax.Liability.builder()
            .setType(SessionCreateParams.AutomaticTax.Liability.Type.SELF)
            .build()
        )
        .build()
    )
    .setSubscriptionData(
      SessionCreateParams.SubscriptionData.builder()
        .setTransferData(
          SessionCreateParams.SubscriptionData.TransferData.builder()
            .setDestination("{{CONNECTEDACCOUNT_ID}}")
            .build()
        )
        .setInvoiceSettings(
          SessionCreateParams.SubscriptionData.InvoiceSettings.builder()
            .setIssuer(
              SessionCreateParams.SubscriptionData.InvoiceSettings.Issuer.builder()
                .setType(
                  SessionCreateParams.SubscriptionData.InvoiceSettings.Issuer.Type.SELF
                )
                .build()
            )
            .build()
        )
        .build()
    )
    .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
    .setSuccessUrl("https://example.com/success")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session = client.v1().checkout().sessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  automatic_tax: {
    enabled: true,
    liability: {
      type: 'self',
    },
  },
  subscription_data: {
    transfer_data: {
      destination: '{{CONNECTEDACCOUNT_ID}}',
    },
    invoice_settings: {
      issuer: {
        type: 'self',
      },
    },
  },
  mode: 'subscription',
  success_url: 'https://example.com/success',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(1),
    },
  },
  AutomaticTax: &stripe.CheckoutSessionCreateAutomaticTaxParams{
    Enabled: stripe.Bool(true),
    Liability: &stripe.CheckoutSessionCreateAutomaticTaxLiabilityParams{
      Type: stripe.String(stripe.CheckoutSessionAutomaticTaxLiabilityTypeSelf),
    },
  },
  SubscriptionData: &stripe.CheckoutSessionCreateSubscriptionDataParams{
    TransferData: &stripe.CheckoutSessionCreateSubscriptionDataTransferDataParams{
      Destination: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
    },
    InvoiceSettings: &stripe.CheckoutSessionCreateSubscriptionDataInvoiceSettingsParams{
      Issuer: &stripe.CheckoutSessionCreateSubscriptionDataInvoiceSettingsIssuerParams{
        Type: stripe.String("self"),
      },
    },
  },
  Mode: stripe.String(stripe.CheckoutSessionModeSubscription),
  SuccessURL: stripe.String("https://example.com/success"),
}
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionCreateOptions
{
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            Price = "{{PRICE_ID}}",
            Quantity = 1,
        },
    },
    AutomaticTax = new Stripe.Checkout.SessionAutomaticTaxOptions
    {
        Enabled = true,
        Liability = new Stripe.Checkout.SessionAutomaticTaxLiabilityOptions
        {
            Type = "self",
        },
    },
    SubscriptionData = new Stripe.Checkout.SessionSubscriptionDataOptions
    {
        TransferData = new Stripe.Checkout.SessionSubscriptionDataTransferDataOptions
        {
            Destination = "{{CONNECTEDACCOUNT_ID}}",
        },
        InvoiceSettings = new Stripe.Checkout.SessionSubscriptionDataInvoiceSettingsOptions
        {
            Issuer = new Stripe.Checkout.SessionSubscriptionDataInvoiceSettingsIssuerOptions
            {
                Type = "self",
            },
        },
    },
    Mode = "subscription",
    SuccessUrl = "https://example.com/success",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

#### Separate charges and transfers

For the Checkout Sessions API calls:

- Include [automatic_tax[liability]](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-automatic_tax-liability) with `type=self`.
- Include [subscription_data[invoice_settings][issuer]](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-subscription_data-invoice_settings-issuer) with `type=self`.
- (Optional) If the connected account is the [settlement merchant](https://docs.stripe.com/connect/separate-charges-and-transfers.md#settlement-merchant), include [subscription_data[on_behalf_of]](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-subscription_data-on_behalf_of) with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=self \
  -d "subscription_data[invoice_settings][issuer][type]"=self \
  -d mode=subscription \
  --data-urlencode success_url="https://example.com/success"
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=self \
  -d "subscription_data[invoice_settings][issuer][type]"=self \
  --mode=subscription \
  --success-url="https://example.com/success"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  automatic_tax: {
    enabled: true,
    liability: {type: 'self'},
  },
  subscription_data: {invoice_settings: {issuer: {type: 'self'}}},
  mode: 'subscription',
  success_url: 'https://example.com/success',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 1}],
  "automatic_tax": {"enabled": True, "liability": {"type": "self"}},
  "subscription_data": {"invoice_settings": {"issuer": {"type": "self"}}},
  "mode": "subscription",
  "success_url": "https://example.com/success",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'line_items' => [
    [
      'price' => '{{PRICE_ID}}',
      'quantity' => 1,
    ],
  ],
  'automatic_tax' => [
    'enabled' => true,
    'liability' => ['type' => 'self'],
  ],
  'subscription_data' => ['invoice_settings' => ['issuer' => ['type' => 'self']]],
  'mode' => 'subscription',
  'success_url' => 'https://example.com/success',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPrice("{{PRICE_ID}}")
        .setQuantity(1L)
        .build()
    )
    .setAutomaticTax(
      SessionCreateParams.AutomaticTax.builder()
        .setEnabled(true)
        .setLiability(
          SessionCreateParams.AutomaticTax.Liability.builder()
            .setType(SessionCreateParams.AutomaticTax.Liability.Type.SELF)
            .build()
        )
        .build()
    )
    .setSubscriptionData(
      SessionCreateParams.SubscriptionData.builder()
        .setInvoiceSettings(
          SessionCreateParams.SubscriptionData.InvoiceSettings.builder()
            .setIssuer(
              SessionCreateParams.SubscriptionData.InvoiceSettings.Issuer.builder()
                .setType(
                  SessionCreateParams.SubscriptionData.InvoiceSettings.Issuer.Type.SELF
                )
                .build()
            )
            .build()
        )
        .build()
    )
    .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
    .setSuccessUrl("https://example.com/success")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session = client.v1().checkout().sessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  automatic_tax: {
    enabled: true,
    liability: {
      type: 'self',
    },
  },
  subscription_data: {
    invoice_settings: {
      issuer: {
        type: 'self',
      },
    },
  },
  mode: 'subscription',
  success_url: 'https://example.com/success',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(1),
    },
  },
  AutomaticTax: &stripe.CheckoutSessionCreateAutomaticTaxParams{
    Enabled: stripe.Bool(true),
    Liability: &stripe.CheckoutSessionCreateAutomaticTaxLiabilityParams{
      Type: stripe.String(stripe.CheckoutSessionAutomaticTaxLiabilityTypeSelf),
    },
  },
  SubscriptionData: &stripe.CheckoutSessionCreateSubscriptionDataParams{
    InvoiceSettings: &stripe.CheckoutSessionCreateSubscriptionDataInvoiceSettingsParams{
      Issuer: &stripe.CheckoutSessionCreateSubscriptionDataInvoiceSettingsIssuerParams{
        Type: stripe.String("self"),
      },
    },
  },
  Mode: stripe.String(stripe.CheckoutSessionModeSubscription),
  SuccessURL: stripe.String("https://example.com/success"),
}
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionCreateOptions
{
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            Price = "{{PRICE_ID}}",
            Quantity = 1,
        },
    },
    AutomaticTax = new Stripe.Checkout.SessionAutomaticTaxOptions
    {
        Enabled = true,
        Liability = new Stripe.Checkout.SessionAutomaticTaxLiabilityOptions
        {
            Type = "self",
        },
    },
    SubscriptionData = new Stripe.Checkout.SessionSubscriptionDataOptions
    {
        InvoiceSettings = new Stripe.Checkout.SessionSubscriptionDataInvoiceSettingsOptions
        {
            Issuer = new Stripe.Checkout.SessionSubscriptionDataInvoiceSettingsIssuerOptions
            {
                Type = "self",
            },
        },
    },
    Mode = "subscription",
    SuccessUrl = "https://example.com/success",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

For the Transfers API calls:

- Include [source_transaction](https://docs.stripe.com/api/transfers/create.md#create_transfer-source_transaction) to tie the transfer to the PaymentIntent created by the Checkout Session.
- Include [destination](https://docs.stripe.com/api/transfers/create.md#create_transfer-destination) with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/transfers \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d amount=1000 \
  -d currency=usd \
  -d source_transaction="{{CHARGE_ID}}" \
  -d destination="{{CONNECTEDACCOUNT_ID}}"
```

```cli
stripe transfers create  \
  --amount=1000 \
  --currency=usd \
  --source-transaction="{{CHARGE_ID}}" \
  --destination="{{CONNECTEDACCOUNT_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

transfer = client.v1.transfers.create({
  amount: 1000,
  currency: 'usd',
  source_transaction: '{{CHARGE_ID}}',
  destination: '{{CONNECTEDACCOUNT_ID}}',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
transfer = client.v1.transfers.create({
  "amount": 1000,
  "currency": "usd",
  "source_transaction": "{{CHARGE_ID}}",
  "destination": "{{CONNECTEDACCOUNT_ID}}",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$transfer = $stripe->transfers->create([
  'amount' => 1000,
  'currency' => 'usd',
  'source_transaction' => '{{CHARGE_ID}}',
  'destination' => '{{CONNECTEDACCOUNT_ID}}',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

TransferCreateParams params =
  TransferCreateParams.builder()
    .setAmount(1000L)
    .setCurrency("usd")
    .setSourceTransaction("{{CHARGE_ID}}")
    .setDestination("{{CONNECTEDACCOUNT_ID}}")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Transfer transfer = client.v1().transfers().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const transfer = await stripe.transfers.create({
  amount: 1000,
  currency: 'usd',
  source_transaction: '{{CHARGE_ID}}',
  destination: '{{CONNECTEDACCOUNT_ID}}',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TransferCreateParams{
  Amount: stripe.Int64(1000),
  Currency: stripe.String(stripe.CurrencyUSD),
  SourceTransaction: stripe.String("{{CHARGE_ID}}"),
  Destination: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
}
result, err := sc.V1Transfers.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new TransferCreateOptions
{
    Amount = 1000,
    Currency = "usd",
    SourceTransaction = "{{CHARGE_ID}}",
    Destination = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Transfers;
Transfer transfer = service.Create(options);
```

### Billing

### Subscriptions

Pick one of the currently supported charge types that allow your platform account to be *liable for tax* (The responsibility for collecting and reporting taxes for transactions in a Connect integration. It can belong to the platform or to connected accounts, depending on your business model, government regulations, and individual transaction details) with [Stripe Subscriptions](https://docs.stripe.com/tax/subscriptions.md):

#### Direct charges

This charge type isn’t supported for use cases involving a connected account where the platform is *liable for tax* (The responsibility for collecting and reporting taxes for transactions in a Connect integration. It can belong to the platform or to connected accounts, depending on your business model, government regulations, and individual transaction details).

#### Destination charges

For the Subscriptions API calls:

- Include [automatic_tax[liability]](https://docs.stripe.com/api/subscriptions/create.md#create_subscription-automatic_tax-liability) with `type=self`.
- Include [transfer_data[destination]](https://docs.stripe.com/api/subscriptions/create.md#create_subscription-transfer_data-destination) with the value of the connected account ID.
- Include [invoice_settings[issuer]](https://docs.stripe.com/api/subscriptions/create.md#create_subscription-invoice_settings-issuer) with `type=self`. In some jurisdictions, like the European Union, invoice PDFs are used as the tax instrument and the invoice issuer must match the entity *liable for tax* (The responsibility for collecting and reporting taxes for transactions in a Connect integration. It can belong to the platform or to connected accounts, depending on your business model, government regulations, and individual transaction details) at all times.
- (Optional) If the connected account is the [settlement merchant](https://docs.stripe.com/connect/destination-charges.md#settlement-merchant), include [on_behalf_of](https://docs.stripe.com/api/subscriptions/create.md#create_subscription-on_behalf_of) with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/subscriptions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "items[0][price]"="{{PRICE_ID}}" \
  -d "items[0][quantity]"=1 \
  -d customer="{{CUSTOMER_ID}}" \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=self \
  -d "transfer_data[destination]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "invoice_settings[issuer][type]"=self
```

```cli
stripe subscriptions create  \
  -d "items[0][price]"="{{PRICE_ID}}" \
  -d "items[0][quantity]"=1 \
  --customer="{{CUSTOMER_ID}}" \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=self \
  -d "transfer_data[destination]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "invoice_settings[issuer][type]"=self
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

subscription = client.v1.subscriptions.create({
  items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  customer: '{{CUSTOMER_ID}}',
  automatic_tax: {
    enabled: true,
    liability: {type: 'self'},
  },
  transfer_data: {destination: '{{CONNECTEDACCOUNT_ID}}'},
  invoice_settings: {issuer: {type: 'self'}},
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
subscription = client.v1.subscriptions.create({
  "items": [{"price": "{{PRICE_ID}}", "quantity": 1}],
  "customer": "{{CUSTOMER_ID}}",
  "automatic_tax": {"enabled": True, "liability": {"type": "self"}},
  "transfer_data": {"destination": "{{CONNECTEDACCOUNT_ID}}"},
  "invoice_settings": {"issuer": {"type": "self"}},
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$subscription = $stripe->subscriptions->create([
  'items' => [
    [
      'price' => '{{PRICE_ID}}',
      'quantity' => 1,
    ],
  ],
  'customer' => '{{CUSTOMER_ID}}',
  'automatic_tax' => [
    'enabled' => true,
    'liability' => ['type' => 'self'],
  ],
  'transfer_data' => ['destination' => '{{CONNECTEDACCOUNT_ID}}'],
  'invoice_settings' => ['issuer' => ['type' => 'self']],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SubscriptionCreateParams params =
  SubscriptionCreateParams.builder()
    .addItem(
      SubscriptionCreateParams.Item.builder()
        .setPrice("{{PRICE_ID}}")
        .setQuantity(1L)
        .build()
    )
    .setCustomer("{{CUSTOMER_ID}}")
    .setAutomaticTax(
      SubscriptionCreateParams.AutomaticTax.builder()
        .setEnabled(true)
        .setLiability(
          SubscriptionCreateParams.AutomaticTax.Liability.builder()
            .setType(SubscriptionCreateParams.AutomaticTax.Liability.Type.SELF)
            .build()
        )
        .build()
    )
    .setTransferData(
      SubscriptionCreateParams.TransferData.builder()
        .setDestination("{{CONNECTEDACCOUNT_ID}}")
        .build()
    )
    .setInvoiceSettings(
      SubscriptionCreateParams.InvoiceSettings.builder()
        .setIssuer(
          SubscriptionCreateParams.InvoiceSettings.Issuer.builder()
            .setType(SubscriptionCreateParams.InvoiceSettings.Issuer.Type.SELF)
            .build()
        )
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Subscription subscription = client.v1().subscriptions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const subscription = await stripe.subscriptions.create({
  items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  customer: '{{CUSTOMER_ID}}',
  automatic_tax: {
    enabled: true,
    liability: {
      type: 'self',
    },
  },
  transfer_data: {
    destination: '{{CONNECTEDACCOUNT_ID}}',
  },
  invoice_settings: {
    issuer: {
      type: 'self',
    },
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.SubscriptionCreateParams{
  Items: []*stripe.SubscriptionCreateItemParams{
    &stripe.SubscriptionCreateItemParams{
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(1),
    },
  },
  Customer: stripe.String("{{CUSTOMER_ID}}"),
  AutomaticTax: &stripe.SubscriptionCreateAutomaticTaxParams{
    Enabled: stripe.Bool(true),
    Liability: &stripe.SubscriptionCreateAutomaticTaxLiabilityParams{
      Type: stripe.String(stripe.SubscriptionAutomaticTaxLiabilityTypeSelf),
    },
  },
  TransferData: &stripe.SubscriptionCreateTransferDataParams{
    Destination: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
  },
  InvoiceSettings: &stripe.SubscriptionCreateInvoiceSettingsParams{
    Issuer: &stripe.SubscriptionCreateInvoiceSettingsIssuerParams{
      Type: stripe.String(stripe.SubscriptionInvoiceSettingsIssuerTypeSelf),
    },
  },
}
result, err := sc.V1Subscriptions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new SubscriptionCreateOptions
{
    Items = new List<SubscriptionItemOptions>
    {
        new SubscriptionItemOptions { Price = "{{PRICE_ID}}", Quantity = 1 },
    },
    Customer = "{{CUSTOMER_ID}}",
    AutomaticTax = new SubscriptionAutomaticTaxOptions
    {
        Enabled = true,
        Liability = new SubscriptionAutomaticTaxLiabilityOptions { Type = "self" },
    },
    TransferData = new SubscriptionTransferDataOptions
    {
        Destination = "{{CONNECTEDACCOUNT_ID}}",
    },
    InvoiceSettings = new SubscriptionInvoiceSettingsOptions
    {
        Issuer = new SubscriptionInvoiceSettingsIssuerOptions { Type = "self" },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Subscriptions;
Subscription subscription = service.Create(options);
```

#### Separate charges and transfers

For the Subscriptions API calls:

- Include [automatic_tax[liability]](https://docs.stripe.com/api/subscriptions/create.md#create_subscription-automatic_tax-liability) with `type=self`.
- Include [invoice_settings[issuer]](https://docs.stripe.com/api/subscriptions/create.md#create_subscription-invoice_settings-issuer) with `type=self`. In some jurisdictions, like the European Union, invoice PDFs are used as the tax instrument and the invoice issuer must match the entity *liable for tax* (The responsibility for collecting and reporting taxes for transactions in a Connect integration. It can belong to the platform or to connected accounts, depending on your business model, government regulations, and individual transaction details) at all times.
- Include [on_behalf_of](https://docs.stripe.com/api/subscriptions/create.md#create_subscription-on_behalf_of) with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/subscriptions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "items[0][price]"="{{PRICE_ID}}" \
  -d "items[0][quantity]"=1 \
  -d customer="{{CUSTOMER_ID}}" \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=self \
  -d "invoice_settings[issuer][type]"=self \
  -d on_behalf_of="{{CONNECTEDACCOUNT_ID}}"
```

```cli
stripe subscriptions create  \
  -d "items[0][price]"="{{PRICE_ID}}" \
  -d "items[0][quantity]"=1 \
  --customer="{{CUSTOMER_ID}}" \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=self \
  -d "invoice_settings[issuer][type]"=self \
  --on-behalf-of="{{CONNECTEDACCOUNT_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

subscription = client.v1.subscriptions.create({
  items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  customer: '{{CUSTOMER_ID}}',
  automatic_tax: {
    enabled: true,
    liability: {type: 'self'},
  },
  invoice_settings: {issuer: {type: 'self'}},
  on_behalf_of: '{{CONNECTEDACCOUNT_ID}}',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
subscription = client.v1.subscriptions.create({
  "items": [{"price": "{{PRICE_ID}}", "quantity": 1}],
  "customer": "{{CUSTOMER_ID}}",
  "automatic_tax": {"enabled": True, "liability": {"type": "self"}},
  "invoice_settings": {"issuer": {"type": "self"}},
  "on_behalf_of": "{{CONNECTEDACCOUNT_ID}}",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$subscription = $stripe->subscriptions->create([
  'items' => [
    [
      'price' => '{{PRICE_ID}}',
      'quantity' => 1,
    ],
  ],
  'customer' => '{{CUSTOMER_ID}}',
  'automatic_tax' => [
    'enabled' => true,
    'liability' => ['type' => 'self'],
  ],
  'invoice_settings' => ['issuer' => ['type' => 'self']],
  'on_behalf_of' => '{{CONNECTEDACCOUNT_ID}}',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SubscriptionCreateParams params =
  SubscriptionCreateParams.builder()
    .addItem(
      SubscriptionCreateParams.Item.builder()
        .setPrice("{{PRICE_ID}}")
        .setQuantity(1L)
        .build()
    )
    .setCustomer("{{CUSTOMER_ID}}")
    .setAutomaticTax(
      SubscriptionCreateParams.AutomaticTax.builder()
        .setEnabled(true)
        .setLiability(
          SubscriptionCreateParams.AutomaticTax.Liability.builder()
            .setType(SubscriptionCreateParams.AutomaticTax.Liability.Type.SELF)
            .build()
        )
        .build()
    )
    .setInvoiceSettings(
      SubscriptionCreateParams.InvoiceSettings.builder()
        .setIssuer(
          SubscriptionCreateParams.InvoiceSettings.Issuer.builder()
            .setType(SubscriptionCreateParams.InvoiceSettings.Issuer.Type.SELF)
            .build()
        )
        .build()
    )
    .setOnBehalfOf("{{CONNECTEDACCOUNT_ID}}")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Subscription subscription = client.v1().subscriptions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const subscription = await stripe.subscriptions.create({
  items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  customer: '{{CUSTOMER_ID}}',
  automatic_tax: {
    enabled: true,
    liability: {
      type: 'self',
    },
  },
  invoice_settings: {
    issuer: {
      type: 'self',
    },
  },
  on_behalf_of: '{{CONNECTEDACCOUNT_ID}}',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.SubscriptionCreateParams{
  Items: []*stripe.SubscriptionCreateItemParams{
    &stripe.SubscriptionCreateItemParams{
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(1),
    },
  },
  Customer: stripe.String("{{CUSTOMER_ID}}"),
  AutomaticTax: &stripe.SubscriptionCreateAutomaticTaxParams{
    Enabled: stripe.Bool(true),
    Liability: &stripe.SubscriptionCreateAutomaticTaxLiabilityParams{
      Type: stripe.String(stripe.SubscriptionAutomaticTaxLiabilityTypeSelf),
    },
  },
  InvoiceSettings: &stripe.SubscriptionCreateInvoiceSettingsParams{
    Issuer: &stripe.SubscriptionCreateInvoiceSettingsIssuerParams{
      Type: stripe.String(stripe.SubscriptionInvoiceSettingsIssuerTypeSelf),
    },
  },
  OnBehalfOf: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
}
result, err := sc.V1Subscriptions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new SubscriptionCreateOptions
{
    Items = new List<SubscriptionItemOptions>
    {
        new SubscriptionItemOptions { Price = "{{PRICE_ID}}", Quantity = 1 },
    },
    Customer = "{{CUSTOMER_ID}}",
    AutomaticTax = new SubscriptionAutomaticTaxOptions
    {
        Enabled = true,
        Liability = new SubscriptionAutomaticTaxLiabilityOptions { Type = "self" },
    },
    InvoiceSettings = new SubscriptionInvoiceSettingsOptions
    {
        Issuer = new SubscriptionInvoiceSettingsIssuerOptions { Type = "self" },
    },
    OnBehalfOf = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Subscriptions;
Subscription subscription = service.Create(options);
```

For the Transfers API calls:

- Include [source_transaction](https://docs.stripe.com/api/transfers/create.md#create_transfer-source_transaction) to tie the transfer to the PaymentIntent created by the Subscription Invoice.
- Include [destination](https://docs.stripe.com/api/transfers/create.md#create_transfer-destination) with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/transfers \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d amount=1000 \
  -d currency=usd \
  -d source_transaction="{{CHARGE_ID}}" \
  -d destination="{{CONNECTEDACCOUNT_ID}}"
```

```cli
stripe transfers create  \
  --amount=1000 \
  --currency=usd \
  --source-transaction="{{CHARGE_ID}}" \
  --destination="{{CONNECTEDACCOUNT_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

transfer = client.v1.transfers.create({
  amount: 1000,
  currency: 'usd',
  source_transaction: '{{CHARGE_ID}}',
  destination: '{{CONNECTEDACCOUNT_ID}}',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
transfer = client.v1.transfers.create({
  "amount": 1000,
  "currency": "usd",
  "source_transaction": "{{CHARGE_ID}}",
  "destination": "{{CONNECTEDACCOUNT_ID}}",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$transfer = $stripe->transfers->create([
  'amount' => 1000,
  'currency' => 'usd',
  'source_transaction' => '{{CHARGE_ID}}',
  'destination' => '{{CONNECTEDACCOUNT_ID}}',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

TransferCreateParams params =
  TransferCreateParams.builder()
    .setAmount(1000L)
    .setCurrency("usd")
    .setSourceTransaction("{{CHARGE_ID}}")
    .setDestination("{{CONNECTEDACCOUNT_ID}}")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Transfer transfer = client.v1().transfers().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const transfer = await stripe.transfers.create({
  amount: 1000,
  currency: 'usd',
  source_transaction: '{{CHARGE_ID}}',
  destination: '{{CONNECTEDACCOUNT_ID}}',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TransferCreateParams{
  Amount: stripe.Int64(1000),
  Currency: stripe.String(stripe.CurrencyUSD),
  SourceTransaction: stripe.String("{{CHARGE_ID}}"),
  Destination: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
}
result, err := sc.V1Transfers.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new TransferCreateOptions
{
    Amount = 1000,
    Currency = "usd",
    SourceTransaction = "{{CHARGE_ID}}",
    Destination = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Transfers;
Transfer transfer = service.Create(options);
```

### Invoicing

Pick one of the currently supported charge types that allow your platform account to be *liable for tax* (The responsibility for collecting and reporting taxes for transactions in a Connect integration. It can belong to the platform or to connected accounts, depending on your business model, government regulations, and individual transaction details) with [Stripe Invoicing](https://docs.stripe.com/tax/invoicing.md):

#### Direct charges

This charge type isn’t supported for use cases involving a connected account where the platform is *liable for tax* (The responsibility for collecting and reporting taxes for transactions in a Connect integration. It can belong to the platform or to connected accounts, depending on your business model, government regulations, and individual transaction details).

#### Destination charges

For the Invoices API calls:

- Include [automatic_tax[liability]](https://docs.stripe.com/api/invoices/create.md#create_invoice-automatic_tax-liability) with `type=self`.
- Include [transfer_data[destination]](https://docs.stripe.com/api/invoices/create.md#create_invoice-transfer_data-destination) with the value of the connected account ID.
- Include [issuer](https://docs.stripe.com/api/invoices/create.md#create_invoice-issuer) with `type=self`. In some jurisdictions, like the European Union, invoice PDFs are used as the tax instrument and the invoice issuer must match the entity *liable for tax* (The responsibility for collecting and reporting taxes for transactions in a Connect integration. It can belong to the platform or to connected accounts, depending on your business model, government regulations, and individual transaction details) at all times.
- (Optional) If the connected account is the [settlement merchant](https://docs.stripe.com/connect/destination-charges.md#settlement-merchant), include [on_behalf_of](https://docs.stripe.com/api/invoices/create.md#create_invoice-on_behalf_of) with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/invoices \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer="{{CUSTOMER_ID}}" \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=self \
  -d "transfer_data[destination]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "issuer[type]"=self
```

```cli
stripe invoices create  \
  --customer="{{CUSTOMER_ID}}" \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=self \
  -d "transfer_data[destination]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "issuer[type]"=self
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

invoice = client.v1.invoices.create({
  customer: '{{CUSTOMER_ID}}',
  automatic_tax: {
    enabled: true,
    liability: {type: 'self'},
  },
  transfer_data: {destination: '{{CONNECTEDACCOUNT_ID}}'},
  issuer: {type: 'self'},
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
invoice = client.v1.invoices.create({
  "customer": "{{CUSTOMER_ID}}",
  "automatic_tax": {"enabled": True, "liability": {"type": "self"}},
  "transfer_data": {"destination": "{{CONNECTEDACCOUNT_ID}}"},
  "issuer": {"type": "self"},
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$invoice = $stripe->invoices->create([
  'customer' => '{{CUSTOMER_ID}}',
  'automatic_tax' => [
    'enabled' => true,
    'liability' => ['type' => 'self'],
  ],
  'transfer_data' => ['destination' => '{{CONNECTEDACCOUNT_ID}}'],
  'issuer' => ['type' => 'self'],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

InvoiceCreateParams params =
  InvoiceCreateParams.builder()
    .setCustomer("{{CUSTOMER_ID}}")
    .setAutomaticTax(
      InvoiceCreateParams.AutomaticTax.builder()
        .setEnabled(true)
        .setLiability(
          InvoiceCreateParams.AutomaticTax.Liability.builder()
            .setType(InvoiceCreateParams.AutomaticTax.Liability.Type.SELF)
            .build()
        )
        .build()
    )
    .setTransferData(
      InvoiceCreateParams.TransferData.builder()
        .setDestination("{{CONNECTEDACCOUNT_ID}}")
        .build()
    )
    .setIssuer(
      InvoiceCreateParams.Issuer.builder()
        .setType(InvoiceCreateParams.Issuer.Type.SELF)
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Invoice invoice = client.v1().invoices().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const invoice = await stripe.invoices.create({
  customer: '{{CUSTOMER_ID}}',
  automatic_tax: {
    enabled: true,
    liability: {
      type: 'self',
    },
  },
  transfer_data: {
    destination: '{{CONNECTEDACCOUNT_ID}}',
  },
  issuer: {
    type: 'self',
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.InvoiceCreateParams{
  Customer: stripe.String("{{CUSTOMER_ID}}"),
  AutomaticTax: &stripe.InvoiceCreateAutomaticTaxParams{
    Enabled: stripe.Bool(true),
    Liability: &stripe.InvoiceCreateAutomaticTaxLiabilityParams{
      Type: stripe.String(stripe.InvoiceAutomaticTaxLiabilityTypeSelf),
    },
  },
  TransferData: &stripe.InvoiceCreateTransferDataParams{
    Destination: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
  },
  Issuer: &stripe.InvoiceCreateIssuerParams{
    Type: stripe.String(stripe.InvoiceIssuerTypeSelf),
  },
}
result, err := sc.V1Invoices.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new InvoiceCreateOptions
{
    Customer = "{{CUSTOMER_ID}}",
    AutomaticTax = new InvoiceAutomaticTaxOptions
    {
        Enabled = true,
        Liability = new InvoiceAutomaticTaxLiabilityOptions { Type = "self" },
    },
    TransferData = new InvoiceTransferDataOptions
    {
        Destination = "{{CONNECTEDACCOUNT_ID}}",
    },
    Issuer = new InvoiceIssuerOptions { Type = "self" },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Invoices;
Invoice invoice = service.Create(options);
```

#### Separate charges and transfers

For the Invoices API calls:

- Include [automatic_tax[liability]](https://docs.stripe.com/api/invoices/create.md#create_invoice-automatic_tax-liability) with `type=self`.
- Include [issuer](https://docs.stripe.com/api/invoices/create.md#create_invoice-issuer) with `type=self`. In some jurisdictions, like the European Union, invoice PDFs are used as the tax instrument and the invoice issuer must match the entity *liable for tax* (The responsibility for collecting and reporting taxes for transactions in a Connect integration. It can belong to the platform or to connected accounts, depending on your business model, government regulations, and individual transaction details) at all times.
- (Optional) If the connected account is the [settlement merchant](https://docs.stripe.com/connect/separate-charges-and-transfers.md#settlement-merchant), include [on_behalf_of](https://docs.stripe.com/api/invoices/create.md#create_invoice-on_behalf_of) with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/invoices \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer="{{CUSTOMER_ID}}" \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=self \
  -d "issuer[type]"=self
```

```cli
stripe invoices create  \
  --customer="{{CUSTOMER_ID}}" \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=self \
  -d "issuer[type]"=self
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

invoice = client.v1.invoices.create({
  customer: '{{CUSTOMER_ID}}',
  automatic_tax: {
    enabled: true,
    liability: {type: 'self'},
  },
  issuer: {type: 'self'},
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
invoice = client.v1.invoices.create({
  "customer": "{{CUSTOMER_ID}}",
  "automatic_tax": {"enabled": True, "liability": {"type": "self"}},
  "issuer": {"type": "self"},
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$invoice = $stripe->invoices->create([
  'customer' => '{{CUSTOMER_ID}}',
  'automatic_tax' => [
    'enabled' => true,
    'liability' => ['type' => 'self'],
  ],
  'issuer' => ['type' => 'self'],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

InvoiceCreateParams params =
  InvoiceCreateParams.builder()
    .setCustomer("{{CUSTOMER_ID}}")
    .setAutomaticTax(
      InvoiceCreateParams.AutomaticTax.builder()
        .setEnabled(true)
        .setLiability(
          InvoiceCreateParams.AutomaticTax.Liability.builder()
            .setType(InvoiceCreateParams.AutomaticTax.Liability.Type.SELF)
            .build()
        )
        .build()
    )
    .setIssuer(
      InvoiceCreateParams.Issuer.builder()
        .setType(InvoiceCreateParams.Issuer.Type.SELF)
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Invoice invoice = client.v1().invoices().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const invoice = await stripe.invoices.create({
  customer: '{{CUSTOMER_ID}}',
  automatic_tax: {
    enabled: true,
    liability: {
      type: 'self',
    },
  },
  issuer: {
    type: 'self',
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.InvoiceCreateParams{
  Customer: stripe.String("{{CUSTOMER_ID}}"),
  AutomaticTax: &stripe.InvoiceCreateAutomaticTaxParams{
    Enabled: stripe.Bool(true),
    Liability: &stripe.InvoiceCreateAutomaticTaxLiabilityParams{
      Type: stripe.String(stripe.InvoiceAutomaticTaxLiabilityTypeSelf),
    },
  },
  Issuer: &stripe.InvoiceCreateIssuerParams{
    Type: stripe.String(stripe.InvoiceIssuerTypeSelf),
  },
}
result, err := sc.V1Invoices.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new InvoiceCreateOptions
{
    Customer = "{{CUSTOMER_ID}}",
    AutomaticTax = new InvoiceAutomaticTaxOptions
    {
        Enabled = true,
        Liability = new InvoiceAutomaticTaxLiabilityOptions { Type = "self" },
    },
    Issuer = new InvoiceIssuerOptions { Type = "self" },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Invoices;
Invoice invoice = service.Create(options);
```

For the Transfers API calls:

- Include [source_transaction](https://docs.stripe.com/api/transfers/create.md#create_transfer-source_transaction) to tie the transfer to the PaymentIntent created by the Invoice.
- Include [destination](https://docs.stripe.com/api/transfers/create.md#create_transfer-destination) with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/transfers \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d amount=1000 \
  -d currency=usd \
  -d source_transaction="{{CHARGE_ID}}" \
  -d destination="{{CONNECTEDACCOUNT_ID}}"
```

```cli
stripe transfers create  \
  --amount=1000 \
  --currency=usd \
  --source-transaction="{{CHARGE_ID}}" \
  --destination="{{CONNECTEDACCOUNT_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

transfer = client.v1.transfers.create({
  amount: 1000,
  currency: 'usd',
  source_transaction: '{{CHARGE_ID}}',
  destination: '{{CONNECTEDACCOUNT_ID}}',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
transfer = client.v1.transfers.create({
  "amount": 1000,
  "currency": "usd",
  "source_transaction": "{{CHARGE_ID}}",
  "destination": "{{CONNECTEDACCOUNT_ID}}",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$transfer = $stripe->transfers->create([
  'amount' => 1000,
  'currency' => 'usd',
  'source_transaction' => '{{CHARGE_ID}}',
  'destination' => '{{CONNECTEDACCOUNT_ID}}',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

TransferCreateParams params =
  TransferCreateParams.builder()
    .setAmount(1000L)
    .setCurrency("usd")
    .setSourceTransaction("{{CHARGE_ID}}")
    .setDestination("{{CONNECTEDACCOUNT_ID}}")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Transfer transfer = client.v1().transfers().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const transfer = await stripe.transfers.create({
  amount: 1000,
  currency: 'usd',
  source_transaction: '{{CHARGE_ID}}',
  destination: '{{CONNECTEDACCOUNT_ID}}',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TransferCreateParams{
  Amount: stripe.Int64(1000),
  Currency: stripe.String(stripe.CurrencyUSD),
  SourceTransaction: stripe.String("{{CHARGE_ID}}"),
  Destination: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
}
result, err := sc.V1Transfers.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new TransferCreateOptions
{
    Amount = 1000,
    Currency = "usd",
    SourceTransaction = "{{CHARGE_ID}}",
    Destination = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Transfers;
Transfer transfer = service.Create(options);
```

### Custom flows using the Stripe Tax API

### PaymentIntents

Pick one of the currently supported charge types that allow your platform account to be *liable for tax* (The responsibility for collecting and reporting taxes for transactions in a Connect integration. It can belong to the platform or to connected accounts, depending on your business model, government regulations, and individual transaction details) with [Stripe Tax API](https://docs.stripe.com/tax/custom.md):

#### Direct charges

We don’t recommend this charge type because the connected account controls refunds. The connected account needs to be aware of the tax withholding strategy to refund the correct amount to you and their users.

#### Destination charges

For the Tax Calculation API calls:

```curl
curl https://api.stripe.com/v1/tax/calculations \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d currency=usd \
  -d "line_items[0][amount]"=1000 \
  -d "line_items[0][reference]"=L1 \
  -d customer="{{CUSTOMER_ID}}"
```

```cli
stripe tax calculations create  \
  --currency=usd \
  -d "line_items[0][amount]"=1000 \
  -d "line_items[0][reference]"=L1 \
  --customer="{{CUSTOMER_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

calculation = client.v1.tax.calculations.create({
  currency: 'usd',
  line_items: [
    {
      amount: 1000,
      reference: 'L1',
    },
  ],
  customer: '{{CUSTOMER_ID}}',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
calculation = client.v1.tax.calculations.create({
  "currency": "usd",
  "line_items": [{"amount": 1000, "reference": "L1"}],
  "customer": "{{CUSTOMER_ID}}",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$calculation = $stripe->tax->calculations->create([
  'currency' => 'usd',
  'line_items' => [
    [
      'amount' => 1000,
      'reference' => 'L1',
    ],
  ],
  'customer' => '{{CUSTOMER_ID}}',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CalculationCreateParams params =
  CalculationCreateParams.builder()
    .setCurrency("usd")
    .addLineItem(
      CalculationCreateParams.LineItem.builder()
        .setAmount(1000L)
        .setReference("L1")
        .build()
    )
    .setCustomer("{{CUSTOMER_ID}}")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Calculation calculation = client.v1().tax().calculations().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const calculation = await stripe.tax.calculations.create({
  currency: 'usd',
  line_items: [
    {
      amount: 1000,
      reference: 'L1',
    },
  ],
  customer: '{{CUSTOMER_ID}}',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TaxCalculationCreateParams{
  Currency: stripe.String(stripe.CurrencyUSD),
  LineItems: []*stripe.TaxCalculationCreateLineItemParams{
    &stripe.TaxCalculationCreateLineItemParams{
      Amount: stripe.Int64(1000),
      Reference: stripe.String("L1"),
    },
  },
  Customer: stripe.String("{{CUSTOMER_ID}}"),
}
result, err := sc.V1TaxCalculations.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Tax.CalculationCreateOptions
{
    Currency = "usd",
    LineItems = new List<Stripe.Tax.CalculationLineItemOptions>
    {
        new Stripe.Tax.CalculationLineItemOptions { Amount = 1000, Reference = "L1" },
    },
    Customer = "{{CUSTOMER_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Tax.Calculations;
Stripe.Tax.Calculation calculation = service.Create(options);
```

When you create a PaymentIntent:

- Include [amount](https://docs.stripe.com/api/payment_intents/create.md#create_payment_intent-amount) with the `amount_total` returned by the tax calculation.
- Include [metadata[tax_calculation]](https://docs.stripe.com/api/payment_intents/create.md#create_payment_intent-metadata) with the `id` returned by the tax calculation.
- Include [transfer_data[destination]](https://docs.stripe.com/api/payment_intents/create.md#create_payment_intent-transfer_data-destination) with the value of the connected account ID.
- (Optional) If the connected account is the [settlement merchant](https://docs.stripe.com/connect/destination-charges.md#settlement-merchant), include [on_behalf_of](https://docs.stripe.com/api/payment_intents/create.md#create_payment_intent-on_behalf_of) with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/payment_intents \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d amount=1000 \
  -d currency=usd \
  -d customer="{{CUSTOMER_ID}}" \
  -d "metadata[tax_calculation]"="{{TAXCALCULATION_ID}}" \
  -d "transfer_data[destination]"="{{CONNECTEDACCOUNT_ID}}"
```

```cli
stripe payment_intents create  \
  --amount=1000 \
  --currency=usd \
  --customer="{{CUSTOMER_ID}}" \
  -d "metadata[tax_calculation]"="{{TAXCALCULATION_ID}}" \
  -d "transfer_data[destination]"="{{CONNECTEDACCOUNT_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

payment_intent = client.v1.payment_intents.create({
  amount: 1000,
  currency: 'usd',
  customer: '{{CUSTOMER_ID}}',
  metadata: {tax_calculation: '{{TAXCALCULATION_ID}}'},
  transfer_data: {destination: '{{CONNECTEDACCOUNT_ID}}'},
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
payment_intent = client.v1.payment_intents.create({
  "amount": 1000,
  "currency": "usd",
  "customer": "{{CUSTOMER_ID}}",
  "metadata": {"tax_calculation": "{{TAXCALCULATION_ID}}"},
  "transfer_data": {"destination": "{{CONNECTEDACCOUNT_ID}}"},
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$paymentIntent = $stripe->paymentIntents->create([
  'amount' => 1000,
  'currency' => 'usd',
  'customer' => '{{CUSTOMER_ID}}',
  'metadata' => ['tax_calculation' => '{{TAXCALCULATION_ID}}'],
  'transfer_data' => ['destination' => '{{CONNECTEDACCOUNT_ID}}'],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

PaymentIntentCreateParams params =
  PaymentIntentCreateParams.builder()
    .setAmount(1000L)
    .setCurrency("usd")
    .setCustomer("{{CUSTOMER_ID}}")
    .putMetadata("tax_calculation", "{{TAXCALCULATION_ID}}")
    .setTransferData(
      PaymentIntentCreateParams.TransferData.builder()
        .setDestination("{{CONNECTEDACCOUNT_ID}}")
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
PaymentIntent paymentIntent = client.v1().paymentIntents().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const paymentIntent = await stripe.paymentIntents.create({
  amount: 1000,
  currency: 'usd',
  customer: '{{CUSTOMER_ID}}',
  metadata: {
    tax_calculation: '{{TAXCALCULATION_ID}}',
  },
  transfer_data: {
    destination: '{{CONNECTEDACCOUNT_ID}}',
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.PaymentIntentCreateParams{
  Amount: stripe.Int64(1000),
  Currency: stripe.String(stripe.CurrencyUSD),
  Customer: stripe.String("{{CUSTOMER_ID}}"),
  TransferData: &stripe.PaymentIntentCreateTransferDataParams{
    Destination: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
  },
}
params.AddMetadata("tax_calculation", "{{TAXCALCULATION_ID}}")
result, err := sc.V1PaymentIntents.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new PaymentIntentCreateOptions
{
    Amount = 1000,
    Currency = "usd",
    Customer = "{{CUSTOMER_ID}}",
    Metadata = new Dictionary<string, string>
    {
        { "tax_calculation", "{{TAXCALCULATION_ID}}" },
    },
    TransferData = new PaymentIntentTransferDataOptions
    {
        Destination = "{{CONNECTEDACCOUNT_ID}}",
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.PaymentIntents;
PaymentIntent paymentIntent = service.Create(options);
```

You must also [create tax transactions](https://docs.stripe.com/tax/custom.md#tax-transaction) to record the tax you collect from customers and [account for refunds](https://docs.stripe.com/tax/custom.md#reversals).

#### Separate charges and transfers

For the Tax Calculation API calls:

```curl
curl https://api.stripe.com/v1/tax/calculations \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d currency=usd \
  -d "line_items[0][amount]"=1000 \
  -d "line_items[0][reference]"=L1 \
  -d customer="{{CUSTOMER_ID}}"
```

```cli
stripe tax calculations create  \
  --currency=usd \
  -d "line_items[0][amount]"=1000 \
  -d "line_items[0][reference]"=L1 \
  --customer="{{CUSTOMER_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

calculation = client.v1.tax.calculations.create({
  currency: 'usd',
  line_items: [
    {
      amount: 1000,
      reference: 'L1',
    },
  ],
  customer: '{{CUSTOMER_ID}}',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
calculation = client.v1.tax.calculations.create({
  "currency": "usd",
  "line_items": [{"amount": 1000, "reference": "L1"}],
  "customer": "{{CUSTOMER_ID}}",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$calculation = $stripe->tax->calculations->create([
  'currency' => 'usd',
  'line_items' => [
    [
      'amount' => 1000,
      'reference' => 'L1',
    ],
  ],
  'customer' => '{{CUSTOMER_ID}}',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CalculationCreateParams params =
  CalculationCreateParams.builder()
    .setCurrency("usd")
    .addLineItem(
      CalculationCreateParams.LineItem.builder()
        .setAmount(1000L)
        .setReference("L1")
        .build()
    )
    .setCustomer("{{CUSTOMER_ID}}")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Calculation calculation = client.v1().tax().calculations().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const calculation = await stripe.tax.calculations.create({
  currency: 'usd',
  line_items: [
    {
      amount: 1000,
      reference: 'L1',
    },
  ],
  customer: '{{CUSTOMER_ID}}',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TaxCalculationCreateParams{
  Currency: stripe.String(stripe.CurrencyUSD),
  LineItems: []*stripe.TaxCalculationCreateLineItemParams{
    &stripe.TaxCalculationCreateLineItemParams{
      Amount: stripe.Int64(1000),
      Reference: stripe.String("L1"),
    },
  },
  Customer: stripe.String("{{CUSTOMER_ID}}"),
}
result, err := sc.V1TaxCalculations.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Tax.CalculationCreateOptions
{
    Currency = "usd",
    LineItems = new List<Stripe.Tax.CalculationLineItemOptions>
    {
        new Stripe.Tax.CalculationLineItemOptions { Amount = 1000, Reference = "L1" },
    },
    Customer = "{{CUSTOMER_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Tax.Calculations;
Stripe.Tax.Calculation calculation = service.Create(options);
```

When you create a PaymentIntent:

- Include [amount](https://docs.stripe.com/api/payment_intents/create.md#create_payment_intent-amount) with the `amount_total` returned by the tax calculation.
- Include [metadata[tax_calculation]](https://docs.stripe.com/api/payment_intents/create.md#create_payment_intent-metadata) with the `id` returned by the tax calculation.
- (Optional) If the connected account is the [settlement merchant](https://docs.stripe.com/connect/separate-charges-and-transfers.md#settlement-merchant), include [on_behalf_of](https://docs.stripe.com/api/payment_intents/create.md#create_payment_intent-on_behalf_of) with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/payment_intents \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d amount=1000 \
  -d currency=usd \
  -d customer="{{CUSTOMER_ID}}" \
  -d "metadata[tax_calculation]"="{{TAXCALCULATION_ID}}"
```

```cli
stripe payment_intents create  \
  --amount=1000 \
  --currency=usd \
  --customer="{{CUSTOMER_ID}}" \
  -d "metadata[tax_calculation]"="{{TAXCALCULATION_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

payment_intent = client.v1.payment_intents.create({
  amount: 1000,
  currency: 'usd',
  customer: '{{CUSTOMER_ID}}',
  metadata: {tax_calculation: '{{TAXCALCULATION_ID}}'},
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
payment_intent = client.v1.payment_intents.create({
  "amount": 1000,
  "currency": "usd",
  "customer": "{{CUSTOMER_ID}}",
  "metadata": {"tax_calculation": "{{TAXCALCULATION_ID}}"},
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$paymentIntent = $stripe->paymentIntents->create([
  'amount' => 1000,
  'currency' => 'usd',
  'customer' => '{{CUSTOMER_ID}}',
  'metadata' => ['tax_calculation' => '{{TAXCALCULATION_ID}}'],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

PaymentIntentCreateParams params =
  PaymentIntentCreateParams.builder()
    .setAmount(1000L)
    .setCurrency("usd")
    .setCustomer("{{CUSTOMER_ID}}")
    .putMetadata("tax_calculation", "{{TAXCALCULATION_ID}}")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
PaymentIntent paymentIntent = client.v1().paymentIntents().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const paymentIntent = await stripe.paymentIntents.create({
  amount: 1000,
  currency: 'usd',
  customer: '{{CUSTOMER_ID}}',
  metadata: {
    tax_calculation: '{{TAXCALCULATION_ID}}',
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.PaymentIntentCreateParams{
  Amount: stripe.Int64(1000),
  Currency: stripe.String(stripe.CurrencyUSD),
  Customer: stripe.String("{{CUSTOMER_ID}}"),
}
params.AddMetadata("tax_calculation", "{{TAXCALCULATION_ID}}")
result, err := sc.V1PaymentIntents.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new PaymentIntentCreateOptions
{
    Amount = 1000,
    Currency = "usd",
    Customer = "{{CUSTOMER_ID}}",
    Metadata = new Dictionary<string, string>
    {
        { "tax_calculation", "{{TAXCALCULATION_ID}}" },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.PaymentIntents;
PaymentIntent paymentIntent = service.Create(options);
```

For the Transfers API calls:

- Include [source_transaction](https://docs.stripe.com/api/transfers/create.md#create_transfer-source_transaction) to tie the transfer to the PaymentIntent.
- Include [destination](https://docs.stripe.com/api/transfers/create.md#create_transfer-destination) with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/transfers \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d amount=1000 \
  -d currency=usd \
  -d source_transaction="{{CHARGE_ID}}" \
  -d destination="{{CONNECTEDACCOUNT_ID}}"
```

```cli
stripe transfers create  \
  --amount=1000 \
  --currency=usd \
  --source-transaction="{{CHARGE_ID}}" \
  --destination="{{CONNECTEDACCOUNT_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

transfer = client.v1.transfers.create({
  amount: 1000,
  currency: 'usd',
  source_transaction: '{{CHARGE_ID}}',
  destination: '{{CONNECTEDACCOUNT_ID}}',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
transfer = client.v1.transfers.create({
  "amount": 1000,
  "currency": "usd",
  "source_transaction": "{{CHARGE_ID}}",
  "destination": "{{CONNECTEDACCOUNT_ID}}",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$transfer = $stripe->transfers->create([
  'amount' => 1000,
  'currency' => 'usd',
  'source_transaction' => '{{CHARGE_ID}}',
  'destination' => '{{CONNECTEDACCOUNT_ID}}',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

TransferCreateParams params =
  TransferCreateParams.builder()
    .setAmount(1000L)
    .setCurrency("usd")
    .setSourceTransaction("{{CHARGE_ID}}")
    .setDestination("{{CONNECTEDACCOUNT_ID}}")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Transfer transfer = client.v1().transfers().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const transfer = await stripe.transfers.create({
  amount: 1000,
  currency: 'usd',
  source_transaction: '{{CHARGE_ID}}',
  destination: '{{CONNECTEDACCOUNT_ID}}',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TransferCreateParams{
  Amount: stripe.Int64(1000),
  Currency: stripe.String(stripe.CurrencyUSD),
  SourceTransaction: stripe.String("{{CHARGE_ID}}"),
  Destination: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
}
result, err := sc.V1Transfers.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new TransferCreateOptions
{
    Amount = 1000,
    Currency = "usd",
    SourceTransaction = "{{CHARGE_ID}}",
    Destination = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Transfers;
Transfer transfer = service.Create(options);
```

You must also [create tax transactions](https://docs.stripe.com/tax/custom.md#tax-transaction) to record the tax you collect from customers and [account for refunds](https://docs.stripe.com/tax/custom.md#reversals).

### Off-Stripe payments

Check how to integrate using [Stripe Tax API](https://docs.stripe.com/tax/custom.md) and to allow your platform account to be *liable for tax* (The responsibility for collecting and reporting taxes for transactions in a Connect integration. It can belong to the platform or to connected accounts, depending on your business model, government regulations, and individual transaction details). In the Tax Calculation API calls:

```curl
curl https://api.stripe.com/v1/tax/calculations \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d currency=usd \
  -d "line_items[0][amount]"=1000 \
  -d "line_items[0][reference]"=L1 \
  -d customer="{{CUSTOMER_ID}}"
```

```cli
stripe tax calculations create  \
  --currency=usd \
  -d "line_items[0][amount]"=1000 \
  -d "line_items[0][reference]"=L1 \
  --customer="{{CUSTOMER_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

calculation = client.v1.tax.calculations.create({
  currency: 'usd',
  line_items: [
    {
      amount: 1000,
      reference: 'L1',
    },
  ],
  customer: '{{CUSTOMER_ID}}',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
calculation = client.v1.tax.calculations.create({
  "currency": "usd",
  "line_items": [{"amount": 1000, "reference": "L1"}],
  "customer": "{{CUSTOMER_ID}}",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$calculation = $stripe->tax->calculations->create([
  'currency' => 'usd',
  'line_items' => [
    [
      'amount' => 1000,
      'reference' => 'L1',
    ],
  ],
  'customer' => '{{CUSTOMER_ID}}',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CalculationCreateParams params =
  CalculationCreateParams.builder()
    .setCurrency("usd")
    .addLineItem(
      CalculationCreateParams.LineItem.builder()
        .setAmount(1000L)
        .setReference("L1")
        .build()
    )
    .setCustomer("{{CUSTOMER_ID}}")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Calculation calculation = client.v1().tax().calculations().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const calculation = await stripe.tax.calculations.create({
  currency: 'usd',
  line_items: [
    {
      amount: 1000,
      reference: 'L1',
    },
  ],
  customer: '{{CUSTOMER_ID}}',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TaxCalculationCreateParams{
  Currency: stripe.String(stripe.CurrencyUSD),
  LineItems: []*stripe.TaxCalculationCreateLineItemParams{
    &stripe.TaxCalculationCreateLineItemParams{
      Amount: stripe.Int64(1000),
      Reference: stripe.String("L1"),
    },
  },
  Customer: stripe.String("{{CUSTOMER_ID}}"),
}
result, err := sc.V1TaxCalculations.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Tax.CalculationCreateOptions
{
    Currency = "usd",
    LineItems = new List<Stripe.Tax.CalculationLineItemOptions>
    {
        new Stripe.Tax.CalculationLineItemOptions { Amount = 1000, Reference = "L1" },
    },
    Customer = "{{CUSTOMER_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Tax.Calculations;
Stripe.Tax.Calculation calculation = service.Create(options);
```

You must also [create tax transactions](https://docs.stripe.com/tax/custom.md#tax-transaction) to record the tax you collect from customers and [account for refunds](https://docs.stripe.com/tax/custom.md#reversals).

After you implement it, Stripe automatically starts collecting tax in jurisdictions where you have an active registration.

> Independent of the payment APIs, we credit the transaction amount to the connected account. You need to withhold the collected tax amount on the platform because the platform is *liable for tax* (The responsibility for collecting and reporting taxes for transactions in a Connect integration. It can belong to the platform or to connected accounts, depending on your business model, government regulations, and individual transaction details).

## Withhold collected tax amount

You must make sure that the tax collected is transferred to your marketplace account, so that you can then remit the tax to relevant jurisdictions.

### Checkout and Payment Links

#### Direct charges

This charge type isn’t supported for use cases involving a connected account where the platform is *liable for tax* (The responsibility for collecting and reporting taxes for transactions in a Connect integration. It can belong to the platform or to connected accounts, depending on your business model, government regulations, and individual transaction details).

#### Destination charges

To withhold the collected tax amount for a Checkout Session or Payment Link integration use a [transfer reversal](https://docs.stripe.com/api/transfer_reversals.md).

For the Transfer Reversal API call, include an [amount](https://docs.stripe.com/api/transfers/create.md#create_transfer-amount) with the value to be reversed from the connected account to your platform equivalent to the [total tax amount](https://docs.stripe.com/api/checkout/sessions/object.md#checkout_session_object-total_details-amount_tax) present in the Checkout Session object.

Obtain the [transfer ID](https://docs.stripe.com/api/charges/object.md#charge_object-transfer) from the Charge object. If you don’t know the charge ID, you can retrieve the Checkout Session and [expand](https://docs.stripe.com/expand.md#multiple-properties) the [PaymentIntent](https://docs.stripe.com/api/checkout/sessions/object.md#checkout_session_object-payment_intent) object and use the [latest charge](https://docs.stripe.com/api/payment_intents/object.md#payment_intent_object-latest_charge) field.

```curl
curl https://api.stripe.com/v1/transfers/{{TRANSFER_ID}}/reversals \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d amount=1000
```

```cli
stripe transfer_reversals create {{TRANSFER_ID}} \
  --amount=1000
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

reversal = client.v1.transfers.reversals.create('{{TRANSFER_ID}}', {amount: 1000})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
reversal = client.v1.transfers.reversals.create(
  "{{TRANSFER_ID}}",
  {"amount": 1000},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$transferReversal = $stripe->transfers->createReversal(
  '{{TRANSFER_ID}}',
  ['amount' => 1000]
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

TransferReversalCreateParams params =
  TransferReversalCreateParams.builder().setAmount(1000L).build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
TransferReversal transferReversal =
  client.v1().transfers().reversals().create("{{TRANSFER_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const transferReversal = await stripe.transfers.createReversal(
  '{{TRANSFER_ID}}',
  {
    amount: 1000,
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TransferReversalCreateParams{
  Amount: stripe.Int64(1000),
  ID: stripe.String("{{TRANSFER_ID}}"),
}
result, err := sc.V1TransferReversals.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new TransferReversalCreateOptions { Amount = 1000 };
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Transfers.Reversals;
TransferReversal transferReversal = service.Create("{{TRANSFER_ID}}", options);
```

To automatically create a Transfer Reversal when the Checkout Session is completed, create a *webhook* (A webhook is a real-time push notification sent to your application as a JSON payload through HTTPS requests) to be notified for [checkout.session.completed](https://docs.stripe.com/api/events/types.md#event_types-checkout.session.completed) events.

This option is best suited for transactions that don’t require currency exchange.

See [Reverse transfers](https://docs.stripe.com/connect/separate-charges-and-transfers.md#reverse-transfers) for more information.

#### Separate charges and transfers

For the Transfer API call, include an [amount](https://docs.stripe.com/api/transfers/create.md#create_transfer-amount) with the value to be transferred to the connected account excluding the [total tax amount](https://docs.stripe.com/api/checkout/sessions/object.md#checkout_session_object-total_details-amount_tax) present in the Checkout Session object. The Checkout Session must be complete.

To obtain the charge ID, you can retrieve the Checkout Session and [expand](https://docs.stripe.com/expand.md#multiple-properties) the [PaymentIntent](https://docs.stripe.com/api/checkout/sessions/object.md#checkout_session_object-payment_intent) object and use the [latest charge](https://docs.stripe.com/api/payment_intents/object.md#payment_intent_object-latest_charge) field.

```curl
curl https://api.stripe.com/v1/transfers \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d amount=1000 \
  -d currency=usd \
  -d source_transaction="{{CHARGE_ID}}" \
  -d destination="{{CONNECTEDACCOUNT_ID}}"
```

```cli
stripe transfers create  \
  --amount=1000 \
  --currency=usd \
  --source-transaction="{{CHARGE_ID}}" \
  --destination="{{CONNECTEDACCOUNT_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

transfer = client.v1.transfers.create({
  amount: 1000,
  currency: 'usd',
  source_transaction: '{{CHARGE_ID}}',
  destination: '{{CONNECTEDACCOUNT_ID}}',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
transfer = client.v1.transfers.create({
  "amount": 1000,
  "currency": "usd",
  "source_transaction": "{{CHARGE_ID}}",
  "destination": "{{CONNECTEDACCOUNT_ID}}",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$transfer = $stripe->transfers->create([
  'amount' => 1000,
  'currency' => 'usd',
  'source_transaction' => '{{CHARGE_ID}}',
  'destination' => '{{CONNECTEDACCOUNT_ID}}',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

TransferCreateParams params =
  TransferCreateParams.builder()
    .setAmount(1000L)
    .setCurrency("usd")
    .setSourceTransaction("{{CHARGE_ID}}")
    .setDestination("{{CONNECTEDACCOUNT_ID}}")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Transfer transfer = client.v1().transfers().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const transfer = await stripe.transfers.create({
  amount: 1000,
  currency: 'usd',
  source_transaction: '{{CHARGE_ID}}',
  destination: '{{CONNECTEDACCOUNT_ID}}',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TransferCreateParams{
  Amount: stripe.Int64(1000),
  Currency: stripe.String(stripe.CurrencyUSD),
  SourceTransaction: stripe.String("{{CHARGE_ID}}"),
  Destination: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
}
result, err := sc.V1Transfers.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new TransferCreateOptions
{
    Amount = 1000,
    Currency = "usd",
    SourceTransaction = "{{CHARGE_ID}}",
    Destination = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Transfers;
Transfer transfer = service.Create(options);
```

Learn more about [separate charges and transfers for Checkout Sessions](https://docs.stripe.com/connect/separate-charges-and-transfers.md?platform=web&ui=stripe-hosted).

### Invoicing

#### Direct charges

This charge type isn’t supported for use cases involving a connected account where the platform is *liable for tax* (The responsibility for collecting and reporting taxes for transactions in a Connect integration. It can belong to the platform or to connected accounts, depending on your business model, government regulations, and individual transaction details).

#### Destination charges

1. **Option 1:** Use `transfer_data[amount]`

To withhold the collected tax amount on destination charges, you can update the Invoice and exclude the tax amount from the `transfer_data[amount]` value.

For the Invoice Update API call, include [transfer_data[amount]](https://docs.stripe.com/api/invoices/update.md#update_invoice-transfer_data-amount) with the value to be transferred to the connected account excluding the [total tax amount](https://docs.stripe.com/api/invoices/object.md#invoice_object-total_tax_amounts-amount) present in the Invoice object. You must make the update before the Invoice is finalized.

```curl
curl https://api.stripe.com/v1/invoices/{{INVOICE_ID}} \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "transfer_data[destination]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "transfer_data[amount]"=1000
```

```cli
stripe invoices update {{INVOICE_ID}} \
  -d "transfer_data[destination]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "transfer_data[amount]"=1000
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

invoice = client.v1.invoices.update(
  '{{INVOICE_ID}}',
  {
    transfer_data: {
      destination: '{{CONNECTEDACCOUNT_ID}}',
      amount: 1000,
    },
  },
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
invoice = client.v1.invoices.update(
  "{{INVOICE_ID}}",
  {"transfer_data": {"destination": "{{CONNECTEDACCOUNT_ID}}", "amount": 1000}},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$invoice = $stripe->invoices->update(
  '{{INVOICE_ID}}',
  [
    'transfer_data' => [
      'destination' => '{{CONNECTEDACCOUNT_ID}}',
      'amount' => 1000,
    ],
  ]
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

InvoiceUpdateParams params =
  InvoiceUpdateParams.builder()
    .setTransferData(
      InvoiceUpdateParams.TransferData.builder()
        .setDestination("{{CONNECTEDACCOUNT_ID}}")
        .setAmount(1000L)
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Invoice invoice = client.v1().invoices().update("{{INVOICE_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const invoice = await stripe.invoices.update(
  '{{INVOICE_ID}}',
  {
    transfer_data: {
      destination: '{{CONNECTEDACCOUNT_ID}}',
      amount: 1000,
    },
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.InvoiceUpdateParams{
  TransferData: &stripe.InvoiceUpdateTransferDataParams{
    Destination: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
    Amount: stripe.Int64(1000),
  },
}
result, err := sc.V1Invoices.Update(context.TODO(), "{{INVOICE_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new InvoiceUpdateOptions
{
    TransferData = new InvoiceTransferDataOptions
    {
        Destination = "{{CONNECTEDACCOUNT_ID}}",
        Amount = 1000,
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Invoices;
Invoice invoice = service.Update("{{INVOICE_ID}}", options);
```

1. **Option 2:** Use `application_fee_amount`

Another option to withhold the collected tax amount is to update the Invoice and include the tax amount in the `application_fee_amount` value.

For the Invoice Update API call, include the [application_fee_amount](https://docs.stripe.com/api/invoices/update.md#update_invoice-application_fee_amount) with the value to be held by your platform including the [total tax amount](https://docs.stripe.com/api/invoices/object.md#invoice_object-total_tax_amounts-amount) present in the Invoice object. The update must be made before the Invoice is finalized.

```curl
curl https://api.stripe.com/v1/invoices/{{INVOICE_ID}} \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d application_fee_amount=1000
```

```cli
stripe invoices update {{INVOICE_ID}} \
  --application-fee-amount=1000
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

invoice = client.v1.invoices.update(
  '{{INVOICE_ID}}',
  {application_fee_amount: 1000},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
invoice = client.v1.invoices.update(
  "{{INVOICE_ID}}",
  {"application_fee_amount": 1000},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$invoice = $stripe->invoices->update(
  '{{INVOICE_ID}}',
  ['application_fee_amount' => 1000]
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

InvoiceUpdateParams params =
  InvoiceUpdateParams.builder().setApplicationFeeAmount(1000L).build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Invoice invoice = client.v1().invoices().update("{{INVOICE_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const invoice = await stripe.invoices.update(
  '{{INVOICE_ID}}',
  {
    application_fee_amount: 1000,
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.InvoiceUpdateParams{ApplicationFeeAmount: stripe.Int64(1000)}
result, err := sc.V1Invoices.Update(context.TODO(), "{{INVOICE_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new InvoiceUpdateOptions { ApplicationFeeAmount = 1000 };
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Invoices;
Invoice invoice = service.Update("{{INVOICE_ID}}", options);
```

1. **Option 3:** Create a [transfer reversal](https://docs.stripe.com/api/transfer_reversals.md)

For the Transfer Reversal API call, include the [amount](https://docs.stripe.com/api/transfers/create.md#create_transfer-amount) with the value to be reversed from the connected account to your platform equivalent to the [total tax amount](https://docs.stripe.com/api/invoices/object.md#invoice_object-total_tax_amounts-amount) present in the Invoice object. The Invoice must be paid.

Obtain the [transfer ID](https://docs.stripe.com/api/charges/object.md#charge_object-transfer) from the [expanded](https://docs.stripe.com/expand.md#multiple-properties) [Charge](https://docs.stripe.com/api/invoices/object.md#invoice_object-charge) object present in the Invoice.

```curl
curl https://api.stripe.com/v1/transfers/{{TRANSFER_ID}}/reversals \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d amount=1000
```

```cli
stripe transfer_reversals create {{TRANSFER_ID}} \
  --amount=1000
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

reversal = client.v1.transfers.reversals.create('{{TRANSFER_ID}}', {amount: 1000})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
reversal = client.v1.transfers.reversals.create(
  "{{TRANSFER_ID}}",
  {"amount": 1000},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$transferReversal = $stripe->transfers->createReversal(
  '{{TRANSFER_ID}}',
  ['amount' => 1000]
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

TransferReversalCreateParams params =
  TransferReversalCreateParams.builder().setAmount(1000L).build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
TransferReversal transferReversal =
  client.v1().transfers().reversals().create("{{TRANSFER_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const transferReversal = await stripe.transfers.createReversal(
  '{{TRANSFER_ID}}',
  {
    amount: 1000,
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TransferReversalCreateParams{
  Amount: stripe.Int64(1000),
  ID: stripe.String("{{TRANSFER_ID}}"),
}
result, err := sc.V1TransferReversals.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new TransferReversalCreateOptions { Amount = 1000 };
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Transfers.Reversals;
TransferReversal transferReversal = service.Create("{{TRANSFER_ID}}", options);
```

To automatically create a Transfer Reversal when the Invoice is paid, create a *webhook* (A webhook is a real-time push notification sent to your application as a JSON payload through HTTPS requests) to be notified for [invoice.payment_succeeded](https://docs.stripe.com/api/events/types.md#event_types-invoice.payment_succeeded) events.

This option is best suited for transactions that don’t require currency exchange.

See [Reverse transfers](https://docs.stripe.com/connect/separate-charges-and-transfers.md#reverse-transfers) for more information.

#### Separate charges and transfers

For the Transfer API call, include the [amount](https://docs.stripe.com/api/transfers/create.md#create_transfer-amount) with the value to be transferred to the connected account excluding the [total tax amount](https://docs.stripe.com/api/invoices/object.md#invoice_object-total_tax_amounts-amount) present in the Invoice object. The Invoice must be paid.

You can obtain the [charge ID](https://docs.stripe.com/api/invoices/object.md#invoice_object-charge) from the Invoice object.

```curl
curl https://api.stripe.com/v1/transfers \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d amount=1000 \
  -d currency=usd \
  -d source_transaction="{{CHARGE_ID}}" \
  -d destination="{{CONNECTEDACCOUNT_ID}}"
```

```cli
stripe transfers create  \
  --amount=1000 \
  --currency=usd \
  --source-transaction="{{CHARGE_ID}}" \
  --destination="{{CONNECTEDACCOUNT_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

transfer = client.v1.transfers.create({
  amount: 1000,
  currency: 'usd',
  source_transaction: '{{CHARGE_ID}}',
  destination: '{{CONNECTEDACCOUNT_ID}}',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
transfer = client.v1.transfers.create({
  "amount": 1000,
  "currency": "usd",
  "source_transaction": "{{CHARGE_ID}}",
  "destination": "{{CONNECTEDACCOUNT_ID}}",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$transfer = $stripe->transfers->create([
  'amount' => 1000,
  'currency' => 'usd',
  'source_transaction' => '{{CHARGE_ID}}',
  'destination' => '{{CONNECTEDACCOUNT_ID}}',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

TransferCreateParams params =
  TransferCreateParams.builder()
    .setAmount(1000L)
    .setCurrency("usd")
    .setSourceTransaction("{{CHARGE_ID}}")
    .setDestination("{{CONNECTEDACCOUNT_ID}}")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Transfer transfer = client.v1().transfers().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const transfer = await stripe.transfers.create({
  amount: 1000,
  currency: 'usd',
  source_transaction: '{{CHARGE_ID}}',
  destination: '{{CONNECTEDACCOUNT_ID}}',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TransferCreateParams{
  Amount: stripe.Int64(1000),
  Currency: stripe.String(stripe.CurrencyUSD),
  SourceTransaction: stripe.String("{{CHARGE_ID}}"),
  Destination: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
}
result, err := sc.V1Transfers.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new TransferCreateOptions
{
    Amount = 1000,
    Currency = "usd",
    SourceTransaction = "{{CHARGE_ID}}",
    Destination = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Transfers;
Transfer transfer = service.Create(options);
```

Related guide: [Separate Charges and Transfers](https://docs.stripe.com/connect/separate-charges-and-transfers.md).

### Subscriptions

#### Direct charges

This charge type isn’t supported for use cases involving a connected account where the platform is *liable for tax* (The responsibility for collecting and reporting taxes for transactions in a Connect integration. It can belong to the platform or to connected accounts, depending on your business model, government regulations, and individual transaction details).

#### Destination charges

1. **Option 1:** Use `transfer_data[amount_percent]`

For the Subscription Create API call, include [transfer_data[amount_percent]](https://docs.stripe.com/api/subscriptions/create.md#create_subscription-transfer_data-amount_percent) with the percentage of the subscription invoice total that will be transferred to the destination account, excluding the tax amount. Before you create the Subscription, you can use the [create preview invoice](https://docs.stripe.com/api/invoices/create_preview.md) endpoint to fetch the [total tax amount](https://docs.stripe.com/api/invoices/object.md#invoice_object-total_tax_amounts-amount) present in the Invoice object.

```curl
curl https://api.stripe.com/v1/subscriptions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer="{{CUSTOMER_ID}}" \
  -d "transfer_data[destination]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "transfer_data[amount_percent]"=65
```

```cli
stripe subscriptions create  \
  --customer="{{CUSTOMER_ID}}" \
  -d "transfer_data[destination]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "transfer_data[amount_percent]"=65
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

subscription = client.v1.subscriptions.create({
  customer: '{{CUSTOMER_ID}}',
  transfer_data: {
    destination: '{{CONNECTEDACCOUNT_ID}}',
    amount_percent: 65,
  },
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
subscription = client.v1.subscriptions.create({
  "customer": "{{CUSTOMER_ID}}",
  "transfer_data": {
    "destination": "{{CONNECTEDACCOUNT_ID}}",
    "amount_percent": 65,
  },
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$subscription = $stripe->subscriptions->create([
  'customer' => '{{CUSTOMER_ID}}',
  'transfer_data' => [
    'destination' => '{{CONNECTEDACCOUNT_ID}}',
    'amount_percent' => 65,
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SubscriptionCreateParams params =
  SubscriptionCreateParams.builder()
    .setCustomer("{{CUSTOMER_ID}}")
    .setTransferData(
      SubscriptionCreateParams.TransferData.builder()
        .setDestination("{{CONNECTEDACCOUNT_ID}}")
        .setAmountPercent(new BigDecimal(65))
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Subscription subscription = client.v1().subscriptions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const subscription = await stripe.subscriptions.create({
  customer: '{{CUSTOMER_ID}}',
  transfer_data: {
    destination: '{{CONNECTEDACCOUNT_ID}}',
    amount_percent: 65,
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.SubscriptionCreateParams{
  Customer: stripe.String("{{CUSTOMER_ID}}"),
  TransferData: &stripe.SubscriptionCreateTransferDataParams{
    Destination: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
    AmountPercent: stripe.Float64(65),
  },
}
result, err := sc.V1Subscriptions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new SubscriptionCreateOptions
{
    Customer = "{{CUSTOMER_ID}}",
    TransferData = new SubscriptionTransferDataOptions
    {
        Destination = "{{CONNECTEDACCOUNT_ID}}",
        AmountPercent = 65M,
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Subscriptions;
Subscription subscription = service.Create(options);
```

For the subsequent billing cycles, you can create a [webhook](https://docs.stripe.com/billing/subscriptions/webhooks.md#understand) that listens to the [invoice.created](https://docs.stripe.com/api/events/types.md#event_types-invoice.created) event and you can also update the Invoice, including a [transfer_data[amount]](https://docs.stripe.com/api/invoices/update.md#update_invoice-transfer_data-amount) with the flat amount that will be transferred to the destination account excluding the total tax amount. You must make the update before the Invoice is finalized. Learn more about how [draft invoices finalize](https://docs.stripe.com/invoicing/integration/workflow-transitions.md#finalized).

```curl
curl https://api.stripe.com/v1/invoices/{{INVOICE_ID}} \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "transfer_data[destination]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "transfer_data[amount]"=1000
```

```cli
stripe invoices update {{INVOICE_ID}} \
  -d "transfer_data[destination]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "transfer_data[amount]"=1000
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

invoice = client.v1.invoices.update(
  '{{INVOICE_ID}}',
  {
    transfer_data: {
      destination: '{{CONNECTEDACCOUNT_ID}}',
      amount: 1000,
    },
  },
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
invoice = client.v1.invoices.update(
  "{{INVOICE_ID}}",
  {"transfer_data": {"destination": "{{CONNECTEDACCOUNT_ID}}", "amount": 1000}},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$invoice = $stripe->invoices->update(
  '{{INVOICE_ID}}',
  [
    'transfer_data' => [
      'destination' => '{{CONNECTEDACCOUNT_ID}}',
      'amount' => 1000,
    ],
  ]
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

InvoiceUpdateParams params =
  InvoiceUpdateParams.builder()
    .setTransferData(
      InvoiceUpdateParams.TransferData.builder()
        .setDestination("{{CONNECTEDACCOUNT_ID}}")
        .setAmount(1000L)
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Invoice invoice = client.v1().invoices().update("{{INVOICE_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const invoice = await stripe.invoices.update(
  '{{INVOICE_ID}}',
  {
    transfer_data: {
      destination: '{{CONNECTEDACCOUNT_ID}}',
      amount: 1000,
    },
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.InvoiceUpdateParams{
  TransferData: &stripe.InvoiceUpdateTransferDataParams{
    Destination: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
    Amount: stripe.Int64(1000),
  },
}
result, err := sc.V1Invoices.Update(context.TODO(), "{{INVOICE_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new InvoiceUpdateOptions
{
    TransferData = new InvoiceTransferDataOptions
    {
        Destination = "{{CONNECTEDACCOUNT_ID}}",
        Amount = 1000,
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Invoices;
Invoice invoice = service.Update("{{INVOICE_ID}}", options);
```

1. **Option 2:** Use `application_fee_percent`

For the Subscription Create API call, include the [application_fee_percent](https://docs.stripe.com/api/subscriptions/create.md#create_subscription-application_fee_percent) with the percentage of the subscription invoice total that will be held by your platform, including the tax amount. Before you create the Subscription, you can use the [create preview invoice](https://docs.stripe.com/api/invoices/create_preview.md) endpoint to fetch the [total tax amount](https://docs.stripe.com/api/invoices/object.md#invoice_object-total_tax_amounts-amount) present in the Invoice object.

```curl
curl https://api.stripe.com/v1/subscriptions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer="{{CUSTOMER_ID}}" \
  -d application_fee_percent=35
```

```cli
stripe subscriptions create  \
  --customer="{{CUSTOMER_ID}}" \
  --application-fee-percent=35
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

subscription = client.v1.subscriptions.create({
  customer: '{{CUSTOMER_ID}}',
  application_fee_percent: 35,
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
subscription = client.v1.subscriptions.create({
  "customer": "{{CUSTOMER_ID}}",
  "application_fee_percent": 35,
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$subscription = $stripe->subscriptions->create([
  'customer' => '{{CUSTOMER_ID}}',
  'application_fee_percent' => 35,
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SubscriptionCreateParams params =
  SubscriptionCreateParams.builder()
    .setCustomer("{{CUSTOMER_ID}}")
    .setApplicationFeePercent(new BigDecimal(35))
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Subscription subscription = client.v1().subscriptions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const subscription = await stripe.subscriptions.create({
  customer: '{{CUSTOMER_ID}}',
  application_fee_percent: 35,
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.SubscriptionCreateParams{
  Customer: stripe.String("{{CUSTOMER_ID}}"),
  ApplicationFeePercent: stripe.Float64(35),
}
result, err := sc.V1Subscriptions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new SubscriptionCreateOptions
{
    Customer = "{{CUSTOMER_ID}}",
    ApplicationFeePercent = 35M,
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Subscriptions;
Subscription subscription = service.Create(options);
```

For the subsequent billing cycles, you can create a [webhook](https://docs.stripe.com/billing/subscriptions/webhooks.md#understand) that listens to the [invoice.created](https://docs.stripe.com/api/events/types.md#event_types-invoice.created) event and you can also update the Invoice, including the [application_fee_amount](https://docs.stripe.com/api/invoices/update.md#update_invoice-application_fee_amount) with the flat amount that will be held by your platform including the total tax amount. The update must be made before the Invoice is finalized. Learn more about [Invoice’s status transitions and finalization](https://docs.stripe.com/invoicing/integration/workflow-transitions.md#finalized).

```curl
curl https://api.stripe.com/v1/invoices/{{INVOICE_ID}} \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d application_fee_amount=1000
```

```cli
stripe invoices update {{INVOICE_ID}} \
  --application-fee-amount=1000
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

invoice = client.v1.invoices.update(
  '{{INVOICE_ID}}',
  {application_fee_amount: 1000},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
invoice = client.v1.invoices.update(
  "{{INVOICE_ID}}",
  {"application_fee_amount": 1000},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$invoice = $stripe->invoices->update(
  '{{INVOICE_ID}}',
  ['application_fee_amount' => 1000]
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

InvoiceUpdateParams params =
  InvoiceUpdateParams.builder().setApplicationFeeAmount(1000L).build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Invoice invoice = client.v1().invoices().update("{{INVOICE_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const invoice = await stripe.invoices.update(
  '{{INVOICE_ID}}',
  {
    application_fee_amount: 1000,
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.InvoiceUpdateParams{ApplicationFeeAmount: stripe.Int64(1000)}
result, err := sc.V1Invoices.Update(context.TODO(), "{{INVOICE_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new InvoiceUpdateOptions { ApplicationFeeAmount = 1000 };
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Invoices;
Invoice invoice = service.Update("{{INVOICE_ID}}", options);
```

See [Percent fees and flat fees](https://docs.stripe.com/connect/subscriptions.md#percent-fees-and-flat-fees) to learn more about application fees.

1. **Option 3:** Create a [transfer reversal](https://docs.stripe.com/api/transfer_reversals.md)

For the Transfer Reversal API call, include the [amount](https://docs.stripe.com/api/transfers/create.md#create_transfer-amount) with the value to be reversed from the connected account to your platform equivalent to the [total tax amount](https://docs.stripe.com/api/invoices/object.md#invoice_object-total_tax_amounts-amount) present in the Subscription [latest Invoice](https://docs.stripe.com/api/subscriptions/object.md#subscription_object-latest_invoice) object.

Obtain the [transfer ID](https://docs.stripe.com/api/charges/object.md#charge_object-transfer) from the Charge object. If you don’t know the charge ID, you can retrieve the Subscription, [expand](https://docs.stripe.com/expand.md#multiple-properties) the [latest invoice](https://docs.stripe.com/api/subscriptions/object.md#subscription_object-latest_invoice) and get the [charge ID](https://docs.stripe.com/api/invoices/object.md#invoice_object-charge) from the Invoice object.

```curl
curl https://api.stripe.com/v1/transfers/{{TRANSFER_ID}}/reversals \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d amount=1000
```

```cli
stripe transfer_reversals create {{TRANSFER_ID}} \
  --amount=1000
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

reversal = client.v1.transfers.reversals.create('{{TRANSFER_ID}}', {amount: 1000})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
reversal = client.v1.transfers.reversals.create(
  "{{TRANSFER_ID}}",
  {"amount": 1000},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$transferReversal = $stripe->transfers->createReversal(
  '{{TRANSFER_ID}}',
  ['amount' => 1000]
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

TransferReversalCreateParams params =
  TransferReversalCreateParams.builder().setAmount(1000L).build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
TransferReversal transferReversal =
  client.v1().transfers().reversals().create("{{TRANSFER_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const transferReversal = await stripe.transfers.createReversal(
  '{{TRANSFER_ID}}',
  {
    amount: 1000,
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TransferReversalCreateParams{
  Amount: stripe.Int64(1000),
  ID: stripe.String("{{TRANSFER_ID}}"),
}
result, err := sc.V1TransferReversals.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new TransferReversalCreateOptions { Amount = 1000 };
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Transfers.Reversals;
TransferReversal transferReversal = service.Create("{{TRANSFER_ID}}", options);
```

To automatically create a Transfer Reversal when the Subscription Invoice is paid, create a *webhook* (A webhook is a real-time push notification sent to your application as a JSON payload through HTTPS requests) to be notified of [invoice.payment_succeeded](https://docs.stripe.com/api/events/types.md#event_types-invoice.payment_succeeded) events.

This option is best suited for transactions that don’t require currency exchange.

See [Reverse transfers](https://docs.stripe.com/connect/separate-charges-and-transfers.md#reverse-transfers) for more information.

#### Separate charges and transfers

For the Transfer API call, include the [amount](https://docs.stripe.com/api/transfers/create.md#create_transfer-amount) with the value to be transferred to the connected account excluding the [total tax amount](https://docs.stripe.com/api/invoices/object.md#invoice_object-total_tax_amounts-amount) present in the Subscription [latest Invoice](https://docs.stripe.com/api/subscriptions/object.md#subscription_object-latest_invoice) object.

To obtain the charge ID, you can retrieve the Subscription, [expand](https://docs.stripe.com/expand.md#multiple-properties) the [latest invoice](https://docs.stripe.com/api/subscriptions/object.md#subscription_object-latest_invoice) and get the [charge ID](https://docs.stripe.com/api/invoices/object.md#invoice_object-charge) from the Invoice object.

```curl
curl https://api.stripe.com/v1/transfers \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d amount=1000 \
  -d currency=usd \
  -d source_transaction="{{CHARGE_ID}}" \
  -d destination="{{CONNECTEDACCOUNT_ID}}"
```

```cli
stripe transfers create  \
  --amount=1000 \
  --currency=usd \
  --source-transaction="{{CHARGE_ID}}" \
  --destination="{{CONNECTEDACCOUNT_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

transfer = client.v1.transfers.create({
  amount: 1000,
  currency: 'usd',
  source_transaction: '{{CHARGE_ID}}',
  destination: '{{CONNECTEDACCOUNT_ID}}',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
transfer = client.v1.transfers.create({
  "amount": 1000,
  "currency": "usd",
  "source_transaction": "{{CHARGE_ID}}",
  "destination": "{{CONNECTEDACCOUNT_ID}}",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$transfer = $stripe->transfers->create([
  'amount' => 1000,
  'currency' => 'usd',
  'source_transaction' => '{{CHARGE_ID}}',
  'destination' => '{{CONNECTEDACCOUNT_ID}}',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

TransferCreateParams params =
  TransferCreateParams.builder()
    .setAmount(1000L)
    .setCurrency("usd")
    .setSourceTransaction("{{CHARGE_ID}}")
    .setDestination("{{CONNECTEDACCOUNT_ID}}")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Transfer transfer = client.v1().transfers().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const transfer = await stripe.transfers.create({
  amount: 1000,
  currency: 'usd',
  source_transaction: '{{CHARGE_ID}}',
  destination: '{{CONNECTEDACCOUNT_ID}}',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TransferCreateParams{
  Amount: stripe.Int64(1000),
  Currency: stripe.String(stripe.CurrencyUSD),
  SourceTransaction: stripe.String("{{CHARGE_ID}}"),
  Destination: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
}
result, err := sc.V1Transfers.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new TransferCreateOptions
{
    Amount = 1000,
    Currency = "usd",
    SourceTransaction = "{{CHARGE_ID}}",
    Destination = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Transfers;
Transfer transfer = service.Create(options);
```

See [separate charges and transfers](https://docs.stripe.com/connect/separate-charges-and-transfers.md).

### PaymentIntents with the Stripe Tax API

#### Direct charges

We don’t recommend this charge type because the connected account controls refunds. The connected account needs to be aware of the tax withholding strategy to refund the correct amount to you and their users.

#### Destination charges

1. **Option 1:** Use `transfer_data[amount]`

When you create a PaymentIntent, include [transfer_data[amount]](https://docs.stripe.com/api/payment_intents/create.md#create_payment_intent-transfer_data-amount) with the value to be transferred to the connected account excluding either the total [tax amount exclusive](https://docs.stripe.com/api/tax/calculations/object.md#tax_calculation_object-tax_amount_exclusive) or [tax amount inclusive](https://docs.stripe.com/api/tax/calculations/object.md#tax_calculation_object-tax_amount_inclusive) present in the Tax Calculation object.

```curl
curl https://api.stripe.com/v1/payment_intents \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d amount=10000 \
  -d currency=usd \
  -d "transfer_data[destination]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "transfer_data[amount]"=1000
```

```cli
stripe payment_intents create  \
  --amount=10000 \
  --currency=usd \
  -d "transfer_data[destination]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "transfer_data[amount]"=1000
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

payment_intent = client.v1.payment_intents.create({
  amount: 10000,
  currency: 'usd',
  transfer_data: {
    destination: '{{CONNECTEDACCOUNT_ID}}',
    amount: 1000,
  },
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
payment_intent = client.v1.payment_intents.create({
  "amount": 10000,
  "currency": "usd",
  "transfer_data": {"destination": "{{CONNECTEDACCOUNT_ID}}", "amount": 1000},
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$paymentIntent = $stripe->paymentIntents->create([
  'amount' => 10000,
  'currency' => 'usd',
  'transfer_data' => [
    'destination' => '{{CONNECTEDACCOUNT_ID}}',
    'amount' => 1000,
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

PaymentIntentCreateParams params =
  PaymentIntentCreateParams.builder()
    .setAmount(10000L)
    .setCurrency("usd")
    .setTransferData(
      PaymentIntentCreateParams.TransferData.builder()
        .setDestination("{{CONNECTEDACCOUNT_ID}}")
        .setAmount(1000L)
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
PaymentIntent paymentIntent = client.v1().paymentIntents().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const paymentIntent = await stripe.paymentIntents.create({
  amount: 10000,
  currency: 'usd',
  transfer_data: {
    destination: '{{CONNECTEDACCOUNT_ID}}',
    amount: 1000,
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.PaymentIntentCreateParams{
  Amount: stripe.Int64(10000),
  Currency: stripe.String(stripe.CurrencyUSD),
  TransferData: &stripe.PaymentIntentCreateTransferDataParams{
    Destination: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
    Amount: stripe.Int64(1000),
  },
}
result, err := sc.V1PaymentIntents.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new PaymentIntentCreateOptions
{
    Amount = 10000,
    Currency = "usd",
    TransferData = new PaymentIntentTransferDataOptions
    {
        Destination = "{{CONNECTEDACCOUNT_ID}}",
        Amount = 1000,
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.PaymentIntents;
PaymentIntent paymentIntent = service.Create(options);
```

1. **Option 2:** Use `application_fee_amount`

When you create a PaymentIntent, include the [application_fee_amount](https://docs.stripe.com/api/payment_intents/create.md#create_payment_intent-application_fee_amount) with the value to be held by your platform including either the total [tax amount exclusive](https://docs.stripe.com/api/tax/calculations/object.md#tax_calculation_object-tax_amount_exclusive) or the [tax amount inclusive](https://docs.stripe.com/api/tax/calculations/object.md#tax_calculation_object-tax_amount_inclusive) present in the Tax Calculation object.

```curl
curl https://api.stripe.com/v1/payment_intents \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d amount=10000 \
  -d currency=usd \
  -d application_fee_amount=1000
```

```cli
stripe payment_intents create  \
  --amount=10000 \
  --currency=usd \
  --application-fee-amount=1000
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

payment_intent = client.v1.payment_intents.create({
  amount: 10000,
  currency: 'usd',
  application_fee_amount: 1000,
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
payment_intent = client.v1.payment_intents.create({
  "amount": 10000,
  "currency": "usd",
  "application_fee_amount": 1000,
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$paymentIntent = $stripe->paymentIntents->create([
  'amount' => 10000,
  'currency' => 'usd',
  'application_fee_amount' => 1000,
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

PaymentIntentCreateParams params =
  PaymentIntentCreateParams.builder()
    .setAmount(10000L)
    .setCurrency("usd")
    .setApplicationFeeAmount(1000L)
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
PaymentIntent paymentIntent = client.v1().paymentIntents().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const paymentIntent = await stripe.paymentIntents.create({
  amount: 10000,
  currency: 'usd',
  application_fee_amount: 1000,
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.PaymentIntentCreateParams{
  Amount: stripe.Int64(10000),
  Currency: stripe.String(stripe.CurrencyUSD),
  ApplicationFeeAmount: stripe.Int64(1000),
}
result, err := sc.V1PaymentIntents.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new PaymentIntentCreateOptions
{
    Amount = 10000,
    Currency = "usd",
    ApplicationFeeAmount = 1000,
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.PaymentIntents;
PaymentIntent paymentIntent = service.Create(options);
```

1. **Option 3:** Create [transfer reversal](https://docs.stripe.com/api/transfer_reversals.md)

For the Transfer Reversal API call, include the [amount](https://docs.stripe.com/api/transfers/create.md#create_transfer-amount) with the value to be reversed from the connected account to your platform equivalent to either the total [tax amount exclusive](https://docs.stripe.com/api/tax/calculations/object.md#tax_calculation_object-tax_amount_exclusive) or the [tax amount inclusive](https://docs.stripe.com/api/tax/calculations/object.md#tax_calculation_object-tax_amount_inclusive) present in the Tax Calculation object.

Obtain the [transfer ID](https://docs.stripe.com/api/charges/object.md#charge_object-transfer) from the [expanded](https://docs.stripe.com/expand.md#multiple-properties) [latest charge](https://docs.stripe.com/api/payment_intents/object.md#payment_intent_object-latest_charge) present in the PaymentIntent object.

```curl
curl https://api.stripe.com/v1/transfers/{{TRANSFER_ID}}/reversals \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d amount=1000
```

```cli
stripe transfer_reversals create {{TRANSFER_ID}} \
  --amount=1000
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

reversal = client.v1.transfers.reversals.create('{{TRANSFER_ID}}', {amount: 1000})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
reversal = client.v1.transfers.reversals.create(
  "{{TRANSFER_ID}}",
  {"amount": 1000},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$transferReversal = $stripe->transfers->createReversal(
  '{{TRANSFER_ID}}',
  ['amount' => 1000]
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

TransferReversalCreateParams params =
  TransferReversalCreateParams.builder().setAmount(1000L).build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
TransferReversal transferReversal =
  client.v1().transfers().reversals().create("{{TRANSFER_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const transferReversal = await stripe.transfers.createReversal(
  '{{TRANSFER_ID}}',
  {
    amount: 1000,
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TransferReversalCreateParams{
  Amount: stripe.Int64(1000),
  ID: stripe.String("{{TRANSFER_ID}}"),
}
result, err := sc.V1TransferReversals.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new TransferReversalCreateOptions { Amount = 1000 };
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Transfers.Reversals;
TransferReversal transferReversal = service.Create("{{TRANSFER_ID}}", options);
```

To automatically create a Transfer Reversal when the PaymentIntent succeeds, create a *webhook* (A webhook is a real-time push notification sent to your application as a JSON payload through HTTPS requests) to be notified of [payment_intent.succeeded](https://docs.stripe.com/api/events/types.md#event_types-payment_intent.succeeded) events.

This option is best suited for transactions that don’t require currency exchange.

See [Reverse transfers](https://docs.stripe.com/connect/separate-charges-and-transfers.md#reverse-transfers) for more information.

#### Separate charges and transfers

For the Transfer API call, include the [amount](https://docs.stripe.com/api/transfers/create.md#create_transfer-amount) with the value to be transferred to the connected account excluding either the total [tax amount exclusive](https://docs.stripe.com/api/tax/calculations/object.md#tax_calculation_object-tax_amount_exclusive) or the [tax amount inclusive](https://docs.stripe.com/api/tax/calculations/object.md#tax_calculation_object-tax_amount_inclusive) present in the Tax Calculation object.

You can obtain the [charge ID](https://docs.stripe.com/api/payment_intents/object.md#payment_intent_object-latest_charge) from the PaymentIntent object.

```curl
curl https://api.stripe.com/v1/transfers \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d amount=1000 \
  -d currency=usd \
  -d source_transaction="{{CHARGE_ID}}" \
  -d destination="{{CONNECTEDACCOUNT_ID}}"
```

```cli
stripe transfers create  \
  --amount=1000 \
  --currency=usd \
  --source-transaction="{{CHARGE_ID}}" \
  --destination="{{CONNECTEDACCOUNT_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

transfer = client.v1.transfers.create({
  amount: 1000,
  currency: 'usd',
  source_transaction: '{{CHARGE_ID}}',
  destination: '{{CONNECTEDACCOUNT_ID}}',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
transfer = client.v1.transfers.create({
  "amount": 1000,
  "currency": "usd",
  "source_transaction": "{{CHARGE_ID}}",
  "destination": "{{CONNECTEDACCOUNT_ID}}",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$transfer = $stripe->transfers->create([
  'amount' => 1000,
  'currency' => 'usd',
  'source_transaction' => '{{CHARGE_ID}}',
  'destination' => '{{CONNECTEDACCOUNT_ID}}',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

TransferCreateParams params =
  TransferCreateParams.builder()
    .setAmount(1000L)
    .setCurrency("usd")
    .setSourceTransaction("{{CHARGE_ID}}")
    .setDestination("{{CONNECTEDACCOUNT_ID}}")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Transfer transfer = client.v1().transfers().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const transfer = await stripe.transfers.create({
  amount: 1000,
  currency: 'usd',
  source_transaction: '{{CHARGE_ID}}',
  destination: '{{CONNECTEDACCOUNT_ID}}',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TransferCreateParams{
  Amount: stripe.Int64(1000),
  Currency: stripe.String(stripe.CurrencyUSD),
  SourceTransaction: stripe.String("{{CHARGE_ID}}"),
  Destination: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
}
result, err := sc.V1Transfers.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new TransferCreateOptions
{
    Amount = 1000,
    Currency = "usd",
    SourceTransaction = "{{CHARGE_ID}}",
    Destination = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Transfers;
Transfer transfer = service.Create(options);
```

See [separate charges and transfers](https://docs.stripe.com/connect/separate-charges-and-transfers.md).

## Access Stripe Tax Reports

### Use the Stripe Dashboard

You can use [Stripe Tax reports](https://docs.stripe.com/tax/reports.md) to help you correctly file and remit tax. The platform account can access the Stripe Tax reports using the [Tax Reporting](https://docs.stripe.com/tax/reports.md#how-to-access-data-using-exports-and-reports) functionality in the Stripe Dashboard.

### Use the Stripe API

Platforms can also download the [itemized tax transactions](https://docs.stripe.com/tax/reports.md#itemized-exports) that they’re liable for using the [Report API](https://docs.stripe.com/reports/api.md) with the [tax.transactions.itemized.2](https://docs.stripe.com/reports/report-types/tax.md) report type.

When a platform runs the following command, they download all 2022 transactions that they have sales tax liability for:

```curl
curl https://api.stripe.com/v1/reporting/report_runs \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d report_type="tax.transactions.itemized.2" \
  -d "parameters[interval_start]"=1641013200 \
  -d "parameters[interval_end]"=1672549200
```

```cli
stripe reporting report_runs create  \
  --report-type="tax.transactions.itemized.2" \
  -d "parameters[interval_start]"=1641013200 \
  -d "parameters[interval_end]"=1672549200
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

report_run = client.v1.reporting.report_runs.create({
  report_type: 'tax.transactions.itemized.2',
  parameters: {
    interval_start: 1641013200,
    interval_end: 1672549200,
  },
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
report_run = client.v1.reporting.report_runs.create({
  "report_type": "tax.transactions.itemized.2",
  "parameters": {"interval_start": 1641013200, "interval_end": 1672549200},
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$reportRun = $stripe->reporting->reportRuns->create([
  'report_type' => 'tax.transactions.itemized.2',
  'parameters' => [
    'interval_start' => 1641013200,
    'interval_end' => 1672549200,
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

ReportRunCreateParams params =
  ReportRunCreateParams.builder()
    .setReportType("tax.transactions.itemized.2")
    .setParameters(
      ReportRunCreateParams.Parameters.builder()
        .setIntervalStart(1641013200L)
        .setIntervalEnd(1672549200L)
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
ReportRun reportRun = client.v1().reporting().reportRuns().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const reportRun = await stripe.reporting.reportRuns.create({
  report_type: 'tax.transactions.itemized.2',
  parameters: {
    interval_start: 1641013200,
    interval_end: 1672549200,
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.ReportingReportRunCreateParams{
  ReportType: stripe.String("tax.transactions.itemized.2"),
  Parameters: &stripe.ReportingReportRunCreateParametersParams{
    IntervalStart: stripe.Int64(1641013200),
    IntervalEnd: stripe.Int64(1672549200),
  },
}
result, err := sc.V1ReportingReportRuns.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Reporting.ReportRunCreateOptions
{
    ReportType = "tax.transactions.itemized.2",
    Parameters = new Stripe.Reporting.ReportRunParametersOptions
    {
        IntervalStart = DateTimeOffset.FromUnixTimeSeconds(1641013200).UtcDateTime,
        IntervalEnd = DateTimeOffset.FromUnixTimeSeconds(1672549200).UtcDateTime,
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Reporting.ReportRuns;
Stripe.Reporting.ReportRun reportRun = service.Create(options);
```

## See also

- [Calculate tax in your custom checkout flow](https://docs.stripe.com/tax/custom.md)
