# Tax for software platforms

Learn how to enable Stripe Tax for your connected accounts, and collect tax when the connected account is liable for paying the tax.

Stripe Tax enables businesses to calculate, collect, and report indirect taxes in over [100 countries](https://docs.stripe.com/tax/supported-countries.md), across hundreds of product categories. As a platform, you can use Stripe tax to offer pre-integrated tax compliance to your connected accounts.

Use this guide if your connected accounts are responsible for collecting, filing, and reporting taxes.

1. [Set up your connected accounts for tax](https://docs.stripe.com/tax/tax-for-platforms.md#check-set-up)
1. (Optional) [Assign tax codes to the product catalog](https://docs.stripe.com/tax/tax-for-platforms.md#assign-product-tax-codes)
1. [Integrate tax calculation and collection](https://docs.stripe.com/tax/tax-for-platforms.md#enable-tax-collection)
1. [Access Stripe Tax Reports](https://docs.stripe.com/tax/tax-for-platforms.md#access-reports)

## Set up your connected accounts for tax

As a platform, you must make sure that a connected account has their [tax settings and registrations set up](https://docs.stripe.com/tax/set-up.md) before enabling tax calculations. This can be achieved by:

### Connected account using the Stripe Dashboard

This option is only available to connected accounts with access to the Stripe Dashboard (for example, [Standard](https://docs.stripe.com/connect/standard-accounts.md) accounts). Ask your connected accounts to use the [Stripe Dashboard to add their head office location, preset tax code, and tax registrations](https://docs.stripe.com/tax/set-up.md). You can also collect the head office location and the preset tax code by enabling Stripe Tax in the connected account onboarding. You can make this adjustment in the [Connect onboarding options](https://dashboard.stripe.com/settings/connect/onboarding-options/tax) in the Dashboard.

### Creating a tax interface within your platform

This option allows accounts without access to the Stripe Dashboard (for example, [Custom](https://docs.stripe.com/connect/custom-accounts.md) and [Express](https://docs.stripe.com/connect/express-accounts.md) accounts) to configure Stripe Tax.

Your platform must build an interface and [use the Tax Settings API](https://docs.stripe.com/tax/settings-api.md#updating-settings) to set the head office location and other default values for the connected accounts. And your platform must [use the Tax Registrations API](https://docs.stripe.com/tax/registrations-api.md#adding-registration) to add tax registrations for the locations where the connected accounts have tax obligations.

### Using Connect embedded components within your platform

You can use [Connect embedded components](https://docs.stripe.com/connect/get-started-connect-embedded-components.md) to provide Stripe tax compliance integration to your connected accounts directly on your website with minimal development. Stripe regularly updates our embedded component integrations, so your tax compliance requirements are always current.

Stripe offers two components for tax:

- Tax settings: Collect the details a connected account needs to calculate taxes, like the head office address and the [preset tax code](https://docs.stripe.com/tax/products-prices-tax-codes-tax-behavior.md#product-tax-code).
- Tax registrations: Connected accounts can manage the locations where they have registered with the local tax authority. Adding a registration enables Stripe to calculate and collect taxes in a given location.

To embed tax settings and tax registrations into your website:

1. Use the [embedded components quickstart](https://docs.stripe.com/connect/connect-embedded-components/quickstart.md) to set up your environment.

1. [Create an AccountSession](https://docs.stripe.com/connect/connect-embedded-components/quickstart.md#server-endpoint) with `tax_settings: {enabled: true}` and/or `tax_registrations: {enabled: true}`.

```curl
curl https://api.stripe.com/v1/account_sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d account={{CONNECTED_ACCOUNT_ID}} \
  -d "components[tax_settings][enabled]"=true \
  -d "components[tax_registrations][enabled]"=true
```

```cli
stripe account_sessions create  \
  --account={{CONNECTED_ACCOUNT_ID}} \
  -d "components[tax_settings][enabled]"=true \
  -d "components[tax_registrations][enabled]"=true
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

account_session = client.v1.account_sessions.create({
  account: '{{CONNECTED_ACCOUNT_ID}}',
  components: {
    tax_settings: {enabled: true},
    tax_registrations: {enabled: true},
  },
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
account_session = client.v1.account_sessions.create({
  "account": "{{CONNECTED_ACCOUNT_ID}}",
  "components": {
    "tax_settings": {"enabled": True},
    "tax_registrations": {"enabled": True},
  },
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$accountSession = $stripe->accountSessions->create([
  'account' => '{{CONNECTED_ACCOUNT_ID}}',
  'components' => [
    'tax_settings' => ['enabled' => true],
    'tax_registrations' => ['enabled' => true],
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

AccountSessionCreateParams params =
  AccountSessionCreateParams.builder()
    .setAccount("{{CONNECTED_ACCOUNT_ID}}")
    .setComponents(AccountSessionCreateParams.Components.builder().build())
    .putExtraParam("components[tax_settings][enabled]", true)
    .putExtraParam("components[tax_registrations][enabled]", true)
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
AccountSession accountSession = client.v1().accountSessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const accountSession = await stripe.accountSessions.create({
  account: '{{CONNECTED_ACCOUNT_ID}}',
  components: {
    tax_settings: {
      enabled: true,
    },
    tax_registrations: {
      enabled: true,
    },
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.AccountSessionCreateParams{
  Account: stripe.String("{{CONNECTED_ACCOUNT_ID}}"),
  Components: &stripe.AccountSessionCreateComponentsParams{},
}
params.AddExtra("components[tax_settings][enabled]", true)
params.AddExtra("components[tax_registrations][enabled]", true)
result, err := sc.V1AccountSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new AccountSessionCreateOptions
{
    Account = "{{CONNECTED_ACCOUNT_ID}}",
    Components = new AccountSessionComponentsOptions(),
};
options.AddExtraParam("components[tax_settings][enabled]", true);
options.AddExtraParam("components[tax_registrations][enabled]", true);
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.AccountSessions;
AccountSession accountSession = service.Create(options);
```

1. [Add the tax-settings or tax-registrations component to the DOM](https://docs.stripe.com/connect/connect-embedded-components/quickstart.md#embedded-component).

After creating the account session and [initializing ConnectJS](https://docs.stripe.com/connect/get-started-connect-embedded-components.md#account-sessions), you can render each tax component in the frontend:

#### React

```jsx
// Include this React component
import {
  ConnectTaxSettings,
  ConnectTaxRegistrations,
  ConnectComponentsProvider
} from "@stripe/react-connect-js";

return (
  <ConnectComponentsProvider connectInstance={stripeConnectInstance}>
    <div>
      {/* You can also use a tab layout */}
      <div>
        <h2>Tax Business Details</h2>
        <ConnectTaxSettings />
      <div>
      <div style={{ marginTop: "12px" }}>
        <h2>Tax Registrations</h2>
        <ConnectTaxRegistrations />
      <div>
    </div>
  </ConnectComponentsProvider>
);
```

#### HTML + JavaScript

```js
// Include this element in your HTML
const container = document.getElementById("container");

const taxSettings = instance.create("tax_settings");
container.appendChild(taxSettings);

const taxRegistrations = stripeConnectInstance.create('tax-registrations');
container.appendChild(taxRegistrations);
```

#### Tax settings component preview

Note: The following is a preview/demo component that behaves differently than live mode usage with real connected accounts. The actual component has more functionality than what might appear in this demo component. For example, for connected accounts without Stripe dashboard access (custom accounts), no user authentication is required in production.

The tax settings component allows connected accounts to set their head office address and a [preset tax code](https://docs.stripe.com/tax/products-prices-tax-codes-tax-behavior.md#product-tax-code). Both attributes are required to calculate taxes for the connected account.

#### Tax registrations component

Note: The following is a preview/demo component that behaves differently than live mode usage with real connected accounts. The actual component has more functionality than what might appear in this demo component. For example, for connected accounts without Stripe dashboard access (custom accounts), no user authentication is required in production.

The tax registrations component allows a connected account to manage its tax registrations. If a connected account doesn’t add a tax registration, but calculates tax for that jurisdiction, Stripe Tax returns a tax amount of `0.00` and sets the [taxability reason to `not_collecting`](https://docs.stripe.com/tax/zero-tax.md#not-registered).

Your platform must then check whether connected accounts have configured Stripe Tax to enable tax calculations.

> [Sign in](https://dashboard.stripe.com/login?redirect=https%3A%2F%2Fdocs.stripe.com%2Ftax%2Ftax-for-platforms) to check if your connected accounts are ready to use Stripe Tax.

On the Connected accounts page in your Dashboard, you can [filter the list on accounts that are ready to use Stripe Tax](https://dashboard.stripe.com/connect/accounts/view/v/enabled?status%5B0%5D=enabled&taxSettingsStatus=active). You can also export those accounts with the following Stripe Tax-related columns:

- **Tax Settings Status**: the value `active` indicates that the account is ready to use Stripe Tax. The value `pending` indicates that some required fields are [missing](https://docs.stripe.com/api/tax/settings/object.md#tax_settings_object-status_details-pending-missing_fields).
- **Tax Threshold Status**: the value `exceeded` indicates that the account’s calculated sales or transactions are over the location’s threshold, and the business likely needs to register for tax. For more information, see [Monitor your obligations](https://docs.stripe.com/tax/monitoring.md).
- **Tax Registration Status**: the value `active` indicates that the account has at least one active [tax registration](https://docs.stripe.com/tax/registering.md).

You can also check whether an account has configured Stripe Tax by [using the Tax Settings API](https://docs.stripe.com/tax/settings-api.md#checking-settings).

## Assign tax codes to the product catalog [Optional]

To calculate taxes, Stripe Tax requires classifying products into their tax codes. One way to do this is to [supply a preset tax code for each connected account](https://docs.stripe.com/tax/settings-api.md#updating-settings), which is probably sufficient if your connected accounts typically sell a single category of items.

However, you might offer your users more control by allowing them to map Tax Codes to each product. You can retrieve a list of [supported product tax codes](https://docs.stripe.com/tax/tax-codes.md) from the Stripe [Tax Code API](https://docs.stripe.com/api/tax_codes.md). You can also allow a subset of this list if your connected accounts only sell specific types of products.

## Integrate tax calculation and collection

You need to integrate with Stripe Tax to calculate taxes as part of your checkout flow.

### Payment Links

### Payment Links for one-time payments

Pick one of the currently supported [charge types](https://docs.stripe.com/connect/charges.md#types) that allow the connected account to be *liable for tax* (The responsibility for collecting and reporting taxes for transactions in a Connect integration. It can belong to the platform or to connected accounts, depending on your business model, government regulations, and individual transaction details) with [Stripe Payment Links](https://docs.stripe.com/tax/payment-links.md):

#### Direct charges

For the Payment Links API calls:

- Include the `Stripe-Account` header with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/payment_links \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=2 \
  -d "automatic_tax[enabled]"=true
```

```cli
stripe payment_links create  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=2 \
  -d "automatic_tax[enabled]"=true
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

payment_link = client.v1.payment_links.create(
  {
    line_items: [
      {
        price: '{{PRICE_ID}}',
        quantity: 2,
      },
    ],
    automatic_tax: {enabled: true},
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
payment_link = client.v1.payment_links.create(
  {
    "line_items": [{"price": "{{PRICE_ID}}", "quantity": 2}],
    "automatic_tax": {"enabled": True},
  },
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$paymentLink = $stripe->paymentLinks->create(
  [
    'line_items' => [
      [
        'price' => '{{PRICE_ID}}',
        'quantity' => 2,
      ],
    ],
    'automatic_tax' => ['enabled' => true],
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
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
      PaymentLinkCreateParams.AutomaticTax.builder().setEnabled(true).build()
    )
    .build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

PaymentLink paymentLink = client.v1().paymentLinks().create(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const paymentLink = await stripe.paymentLinks.create(
  {
    line_items: [
      {
        price: '{{PRICE_ID}}',
        quantity: 2,
      },
    ],
    automatic_tax: {
      enabled: true,
    },
  },
  {
    stripeAccount: '{{CONNECTEDACCOUNT_ID}}',
  }
);
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
  AutomaticTax: &stripe.PaymentLinkCreateAutomaticTaxParams{Enabled: stripe.Bool(true)},
}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
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
    AutomaticTax = new PaymentLinkAutomaticTaxOptions { Enabled = true },
};
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.PaymentLinks;
PaymentLink paymentLink = service.Create(options, requestOptions);
```

#### Destination charges

For the Payment Links API calls:

- Include [automatic_tax[liability]](https://docs.stripe.com/api/payment_links/payment_links/create.md#create_payment_link-automatic_tax-liability) with `type=account` and `account` with the value of the connected account ID.
- Include [transfer_data[destination]](https://docs.stripe.com/api/payment_links/payment_links/create.md#create_payment_link-transfer_data) with the value of the connected account ID.
- (Optional) If you’re [automatically sending invoices](https://docs.stripe.com/payment-links/post-payment.md#automatically-send-paid-invoices), include [invoice_creation[invoice_data][issuer]](https://docs.stripe.com/api/payment_links/payment_links/create.md#create_payment_link-invoice_creation-invoice_data-issuer) with `type=account` and `account` with the value of the connected account ID.
- (Optional) If the connected account is the [settlement merchant](https://docs.stripe.com/connect/destination-charges.md#settlement-merchant), include [on_behalf_of](https://docs.stripe.com/api/payment_links/payment_links/create.md#create_payment_link-on_behalf_of) with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/payment_links \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=2 \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=account \
  -d "automatic_tax[liability][account]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "transfer_data[destination]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "invoice_creation[enabled]"=true \
  -d "invoice_creation[invoice_data][issuer][type]"=account \
  -d "invoice_creation[invoice_data][issuer][account]"="{{CONNECTEDACCOUNT_ID}}"
```

```cli
stripe payment_links create  \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=2 \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=account \
  -d "automatic_tax[liability][account]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "transfer_data[destination]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "invoice_creation[enabled]"=true \
  -d "invoice_creation[invoice_data][issuer][type]"=account \
  -d "invoice_creation[invoice_data][issuer][account]"="{{CONNECTEDACCOUNT_ID}}"
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
    liability: {
      type: 'account',
      account: '{{CONNECTEDACCOUNT_ID}}',
    },
  },
  transfer_data: {destination: '{{CONNECTEDACCOUNT_ID}}'},
  invoice_creation: {
    enabled: true,
    invoice_data: {
      issuer: {
        type: 'account',
        account: '{{CONNECTEDACCOUNT_ID}}',
      },
    },
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
  "automatic_tax": {
    "enabled": True,
    "liability": {"type": "account", "account": "{{CONNECTEDACCOUNT_ID}}"},
  },
  "transfer_data": {"destination": "{{CONNECTEDACCOUNT_ID}}"},
  "invoice_creation": {
    "enabled": True,
    "invoice_data": {
      "issuer": {"type": "account", "account": "{{CONNECTEDACCOUNT_ID}}"},
    },
  },
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
    'liability' => [
      'type' => 'account',
      'account' => '{{CONNECTEDACCOUNT_ID}}',
    ],
  ],
  'transfer_data' => ['destination' => '{{CONNECTEDACCOUNT_ID}}'],
  'invoice_creation' => [
    'enabled' => true,
    'invoice_data' => [
      'issuer' => [
        'type' => 'account',
        'account' => '{{CONNECTEDACCOUNT_ID}}',
      ],
    ],
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
            .setType(PaymentLinkCreateParams.AutomaticTax.Liability.Type.ACCOUNT)
            .setAccount("{{CONNECTEDACCOUNT_ID}}")
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
                  PaymentLinkCreateParams.InvoiceCreation.InvoiceData.Issuer.Type.ACCOUNT
                )
                .setAccount("{{CONNECTEDACCOUNT_ID}}")
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
      type: 'account',
      account: '{{CONNECTEDACCOUNT_ID}}',
    },
  },
  transfer_data: {
    destination: '{{CONNECTEDACCOUNT_ID}}',
  },
  invoice_creation: {
    enabled: true,
    invoice_data: {
      issuer: {
        type: 'account',
        account: '{{CONNECTEDACCOUNT_ID}}',
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
      Type: stripe.String(stripe.PaymentLinkAutomaticTaxLiabilityTypeAccount),
      Account: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
    },
  },
  TransferData: &stripe.PaymentLinkCreateTransferDataParams{
    Destination: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
  },
  InvoiceCreation: &stripe.PaymentLinkCreateInvoiceCreationParams{
    Enabled: stripe.Bool(true),
    InvoiceData: &stripe.PaymentLinkCreateInvoiceCreationInvoiceDataParams{
      Issuer: &stripe.PaymentLinkCreateInvoiceCreationInvoiceDataIssuerParams{
        Type: stripe.String(stripe.PaymentLinkInvoiceCreationInvoiceDataIssuerTypeAccount),
        Account: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
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
        Liability = new PaymentLinkAutomaticTaxLiabilityOptions
        {
            Type = "account",
            Account = "{{CONNECTEDACCOUNT_ID}}",
        },
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
                Type = "account",
                Account = "{{CONNECTEDACCOUNT_ID}}",
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

- Include [automatic_tax[liability]](https://docs.stripe.com/api/payment_links/payment_links/create.md#create_payment_link-automatic_tax-liability) with `type=account` and `account` with the value of the connected account ID.
- (Optional) If you’re [automatically sending invoices](https://docs.stripe.com/payment-links/post-payment.md#automatically-send-paid-invoices), include [invoice_creation[invoice_data][issuer]](https://docs.stripe.com/api/payment_links/payment_links/create.md#create_payment_link-invoice_creation-invoice_data-issuer) with `type=account` and `account` with the value of the connected account ID.
- (Optional) If the connected account is the [settlement merchant](https://docs.stripe.com/connect/separate-charges-and-transfers.md#settlement-merchant), include [on_behalf_of](https://docs.stripe.com/api/payment_links/payment_links/create.md#create_payment_link-on_behalf_of) with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/payment_links \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=2 \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=account \
  -d "automatic_tax[liability][account]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "invoice_creation[enabled]"=true \
  -d "invoice_creation[invoice_data][issuer][type]"=account \
  -d "invoice_creation[invoice_data][issuer][account]"="{{CONNECTEDACCOUNT_ID}}"
```

```cli
stripe payment_links create  \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=2 \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=account \
  -d "automatic_tax[liability][account]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "invoice_creation[enabled]"=true \
  -d "invoice_creation[invoice_data][issuer][type]"=account \
  -d "invoice_creation[invoice_data][issuer][account]"="{{CONNECTEDACCOUNT_ID}}"
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
    liability: {
      type: 'account',
      account: '{{CONNECTEDACCOUNT_ID}}',
    },
  },
  invoice_creation: {
    enabled: true,
    invoice_data: {
      issuer: {
        type: 'account',
        account: '{{CONNECTEDACCOUNT_ID}}',
      },
    },
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
  "automatic_tax": {
    "enabled": True,
    "liability": {"type": "account", "account": "{{CONNECTEDACCOUNT_ID}}"},
  },
  "invoice_creation": {
    "enabled": True,
    "invoice_data": {
      "issuer": {"type": "account", "account": "{{CONNECTEDACCOUNT_ID}}"},
    },
  },
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
    'liability' => [
      'type' => 'account',
      'account' => '{{CONNECTEDACCOUNT_ID}}',
    ],
  ],
  'invoice_creation' => [
    'enabled' => true,
    'invoice_data' => [
      'issuer' => [
        'type' => 'account',
        'account' => '{{CONNECTEDACCOUNT_ID}}',
      ],
    ],
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
            .setType(PaymentLinkCreateParams.AutomaticTax.Liability.Type.ACCOUNT)
            .setAccount("{{CONNECTEDACCOUNT_ID}}")
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
                  PaymentLinkCreateParams.InvoiceCreation.InvoiceData.Issuer.Type.ACCOUNT
                )
                .setAccount("{{CONNECTEDACCOUNT_ID}}")
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
      type: 'account',
      account: '{{CONNECTEDACCOUNT_ID}}',
    },
  },
  invoice_creation: {
    enabled: true,
    invoice_data: {
      issuer: {
        type: 'account',
        account: '{{CONNECTEDACCOUNT_ID}}',
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
      Type: stripe.String(stripe.PaymentLinkAutomaticTaxLiabilityTypeAccount),
      Account: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
    },
  },
  InvoiceCreation: &stripe.PaymentLinkCreateInvoiceCreationParams{
    Enabled: stripe.Bool(true),
    InvoiceData: &stripe.PaymentLinkCreateInvoiceCreationInvoiceDataParams{
      Issuer: &stripe.PaymentLinkCreateInvoiceCreationInvoiceDataIssuerParams{
        Type: stripe.String(stripe.PaymentLinkInvoiceCreationInvoiceDataIssuerTypeAccount),
        Account: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
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
        Liability = new PaymentLinkAutomaticTaxLiabilityOptions
        {
            Type = "account",
            Account = "{{CONNECTEDACCOUNT_ID}}",
        },
    },
    InvoiceCreation = new PaymentLinkInvoiceCreationOptions
    {
        Enabled = true,
        InvoiceData = new PaymentLinkInvoiceCreationInvoiceDataOptions
        {
            Issuer = new PaymentLinkInvoiceCreationInvoiceDataIssuerOptions
            {
                Type = "account",
                Account = "{{CONNECTEDACCOUNT_ID}}",
            },
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.PaymentLinks;
PaymentLink paymentLink = service.Create(options);
```

For the Transfers API calls:

- Include [source_transaction](https://docs.stripe.com/api/transfers/create.md#create_transfer-source_transaction) to tie the transfer to the payment intent created by the payment link.
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

Pick one of the currently supported [charge types](https://docs.stripe.com/connect/charges.md#types) that allow the connected account to be *liable for tax* (The responsibility for collecting and reporting taxes for transactions in a Connect integration. It can belong to the platform or to connected accounts, depending on your business model, government regulations, and individual transaction details) with [Stripe Payment Links](https://docs.stripe.com/tax/payment-links.md):

#### Direct charges

For the Payment Links API calls:

- Include the `Stripe-Account` header with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/payment_links \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "automatic_tax[enabled]"=true
```

```cli
stripe payment_links create  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "automatic_tax[enabled]"=true
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

payment_link = client.v1.payment_links.create(
  {
    line_items: [
      {
        price: '{{PRICE_ID}}',
        quantity: 1,
      },
    ],
    automatic_tax: {enabled: true},
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
payment_link = client.v1.payment_links.create(
  {
    "line_items": [{"price": "{{PRICE_ID}}", "quantity": 1}],
    "automatic_tax": {"enabled": True},
  },
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$paymentLink = $stripe->paymentLinks->create(
  [
    'line_items' => [
      [
        'price' => '{{PRICE_ID}}',
        'quantity' => 1,
      ],
    ],
    'automatic_tax' => ['enabled' => true],
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
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
      PaymentLinkCreateParams.AutomaticTax.builder().setEnabled(true).build()
    )
    .build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

PaymentLink paymentLink = client.v1().paymentLinks().create(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const paymentLink = await stripe.paymentLinks.create(
  {
    line_items: [
      {
        price: '{{PRICE_ID}}',
        quantity: 1,
      },
    ],
    automatic_tax: {
      enabled: true,
    },
  },
  {
    stripeAccount: '{{CONNECTEDACCOUNT_ID}}',
  }
);
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
  AutomaticTax: &stripe.PaymentLinkCreateAutomaticTaxParams{Enabled: stripe.Bool(true)},
}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
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
    AutomaticTax = new PaymentLinkAutomaticTaxOptions { Enabled = true },
};
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.PaymentLinks;
PaymentLink paymentLink = service.Create(options, requestOptions);
```

#### Destination charges

For the Payment Links API calls:

- Include [automatic_tax[liability]](https://docs.stripe.com/api/payment_links/payment_links/create.md#create_payment_link-automatic_tax-liability) with `type=account` and `account` with the value of the connected account ID.
- Include [transfer_data[destination]](https://docs.stripe.com/api/payment_links/payment_links/create.md#create_payment_link-transfer_data) with the value of the connected account ID.
- Include [subscription_data[invoice_settings][issuer]](https://docs.stripe.com/api/payment_links/payment_links/create.md#create_payment_link-subscription_data-invoice_settings-issuer) with `type=account` and `account` with the value of the connected account ID.
- (Optional) If the connected account is the [settlement merchant](https://docs.stripe.com/connect/destination-charges.md#settlement-merchant), include [subscription_data[on_behalf_of]](https://docs.stripe.com/api/payment_links/payment_links/create.md#create_payment_link-on_behalf_of) with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/payment_links \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=account \
  -d "automatic_tax[liability][account]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "transfer_data[destination]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "subscription_data[invoice_settings][issuer][type]"=account \
  -d "subscription_data[invoice_settings][issuer][account]"="{{CONNECTEDACCOUNT_ID}}"
```

```cli
stripe payment_links create  \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=account \
  -d "automatic_tax[liability][account]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "transfer_data[destination]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "subscription_data[invoice_settings][issuer][type]"=account \
  -d "subscription_data[invoice_settings][issuer][account]"="{{CONNECTEDACCOUNT_ID}}"
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
    liability: {
      type: 'account',
      account: '{{CONNECTEDACCOUNT_ID}}',
    },
  },
  transfer_data: {destination: '{{CONNECTEDACCOUNT_ID}}'},
  subscription_data: {
    invoice_settings: {
      issuer: {
        type: 'account',
        account: '{{CONNECTEDACCOUNT_ID}}',
      },
    },
  },
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
payment_link = client.v1.payment_links.create({
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 1}],
  "automatic_tax": {
    "enabled": True,
    "liability": {"type": "account", "account": "{{CONNECTEDACCOUNT_ID}}"},
  },
  "transfer_data": {"destination": "{{CONNECTEDACCOUNT_ID}}"},
  "subscription_data": {
    "invoice_settings": {
      "issuer": {"type": "account", "account": "{{CONNECTEDACCOUNT_ID}}"},
    },
  },
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
    'liability' => [
      'type' => 'account',
      'account' => '{{CONNECTEDACCOUNT_ID}}',
    ],
  ],
  'transfer_data' => ['destination' => '{{CONNECTEDACCOUNT_ID}}'],
  'subscription_data' => [
    'invoice_settings' => [
      'issuer' => [
        'type' => 'account',
        'account' => '{{CONNECTEDACCOUNT_ID}}',
      ],
    ],
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
        .setQuantity(1L)
        .build()
    )
    .setAutomaticTax(
      PaymentLinkCreateParams.AutomaticTax.builder()
        .setEnabled(true)
        .setLiability(
          PaymentLinkCreateParams.AutomaticTax.Liability.builder()
            .setType(PaymentLinkCreateParams.AutomaticTax.Liability.Type.ACCOUNT)
            .setAccount("{{CONNECTEDACCOUNT_ID}}")
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
                  PaymentLinkCreateParams.SubscriptionData.InvoiceSettings.Issuer.Type.ACCOUNT
                )
                .setAccount("{{CONNECTEDACCOUNT_ID}}")
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
      type: 'account',
      account: '{{CONNECTEDACCOUNT_ID}}',
    },
  },
  transfer_data: {
    destination: '{{CONNECTEDACCOUNT_ID}}',
  },
  subscription_data: {
    invoice_settings: {
      issuer: {
        type: 'account',
        account: '{{CONNECTEDACCOUNT_ID}}',
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
      Type: stripe.String(stripe.PaymentLinkAutomaticTaxLiabilityTypeAccount),
      Account: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
    },
  },
  TransferData: &stripe.PaymentLinkCreateTransferDataParams{
    Destination: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
  },
  SubscriptionData: &stripe.PaymentLinkCreateSubscriptionDataParams{
    InvoiceSettings: &stripe.PaymentLinkCreateSubscriptionDataInvoiceSettingsParams{
      Issuer: &stripe.PaymentLinkCreateSubscriptionDataInvoiceSettingsIssuerParams{
        Type: stripe.String(stripe.PaymentLinkSubscriptionDataInvoiceSettingsIssuerTypeAccount),
        Account: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
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
        Liability = new PaymentLinkAutomaticTaxLiabilityOptions
        {
            Type = "account",
            Account = "{{CONNECTEDACCOUNT_ID}}",
        },
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
                Type = "account",
                Account = "{{CONNECTEDACCOUNT_ID}}",
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

- Include [automatic_tax[liability]](https://docs.stripe.com/api/payment_links/payment_links/create.md#create_payment_link-automatic_tax-liability) with `type=account` and `account` with the value of the connected account ID.
- Include [subscription_data[invoice_settings][issuer]](https://docs.stripe.com/api/payment_links/payment_links/create.md#create_payment_link-subscription_data-invoice_settings-issuer) with `type=account` and `account` with the value of the connected account ID.
- (Optional) If the connected account is the [settlement merchant](https://docs.stripe.com/connect/separate-charges-and-transfers.md#settlement-merchant), include [subscription_data[on_behalf_of]](https://docs.stripe.com/api/payment_links/payment_links/create.md#create_payment_link-on_behalf_of) with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/payment_links \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=account \
  -d "automatic_tax[liability][account]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "subscription_data[invoice_settings][issuer][type]"=account \
  -d "subscription_data[invoice_settings][issuer][account]"="{{CONNECTEDACCOUNT_ID}}"
```

```cli
stripe payment_links create  \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=account \
  -d "automatic_tax[liability][account]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "subscription_data[invoice_settings][issuer][type]"=account \
  -d "subscription_data[invoice_settings][issuer][account]"="{{CONNECTEDACCOUNT_ID}}"
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
    liability: {
      type: 'account',
      account: '{{CONNECTEDACCOUNT_ID}}',
    },
  },
  subscription_data: {
    invoice_settings: {
      issuer: {
        type: 'account',
        account: '{{CONNECTEDACCOUNT_ID}}',
      },
    },
  },
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
payment_link = client.v1.payment_links.create({
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 1}],
  "automatic_tax": {
    "enabled": True,
    "liability": {"type": "account", "account": "{{CONNECTEDACCOUNT_ID}}"},
  },
  "subscription_data": {
    "invoice_settings": {
      "issuer": {"type": "account", "account": "{{CONNECTEDACCOUNT_ID}}"},
    },
  },
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
    'liability' => [
      'type' => 'account',
      'account' => '{{CONNECTEDACCOUNT_ID}}',
    ],
  ],
  'subscription_data' => [
    'invoice_settings' => [
      'issuer' => [
        'type' => 'account',
        'account' => '{{CONNECTEDACCOUNT_ID}}',
      ],
    ],
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
        .setQuantity(1L)
        .build()
    )
    .setAutomaticTax(
      PaymentLinkCreateParams.AutomaticTax.builder()
        .setEnabled(true)
        .setLiability(
          PaymentLinkCreateParams.AutomaticTax.Liability.builder()
            .setType(PaymentLinkCreateParams.AutomaticTax.Liability.Type.ACCOUNT)
            .setAccount("{{CONNECTEDACCOUNT_ID}}")
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
                  PaymentLinkCreateParams.SubscriptionData.InvoiceSettings.Issuer.Type.ACCOUNT
                )
                .setAccount("{{CONNECTEDACCOUNT_ID}}")
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
      type: 'account',
      account: '{{CONNECTEDACCOUNT_ID}}',
    },
  },
  subscription_data: {
    invoice_settings: {
      issuer: {
        type: 'account',
        account: '{{CONNECTEDACCOUNT_ID}}',
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
      Type: stripe.String(stripe.PaymentLinkAutomaticTaxLiabilityTypeAccount),
      Account: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
    },
  },
  SubscriptionData: &stripe.PaymentLinkCreateSubscriptionDataParams{
    InvoiceSettings: &stripe.PaymentLinkCreateSubscriptionDataInvoiceSettingsParams{
      Issuer: &stripe.PaymentLinkCreateSubscriptionDataInvoiceSettingsIssuerParams{
        Type: stripe.String(stripe.PaymentLinkSubscriptionDataInvoiceSettingsIssuerTypeAccount),
        Account: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
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
        Liability = new PaymentLinkAutomaticTaxLiabilityOptions
        {
            Type = "account",
            Account = "{{CONNECTEDACCOUNT_ID}}",
        },
    },
    SubscriptionData = new PaymentLinkSubscriptionDataOptions
    {
        InvoiceSettings = new PaymentLinkSubscriptionDataInvoiceSettingsOptions
        {
            Issuer = new PaymentLinkSubscriptionDataInvoiceSettingsIssuerOptions
            {
                Type = "account",
                Account = "{{CONNECTEDACCOUNT_ID}}",
            },
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.PaymentLinks;
PaymentLink paymentLink = service.Create(options);
```

For the Transfers API calls:

- Include [source_transaction](https://docs.stripe.com/api/transfers/create.md#create_transfer-source_transaction) to tie the transfer to the payment intent created by the payment link.
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

Pick one of the currently supported [charge types](https://docs.stripe.com/connect/charges.md#types) that allow the connected account to be *liable for tax* (The responsibility for collecting and reporting taxes for transactions in a Connect integration. It can belong to the platform or to connected accounts, depending on your business model, government regulations, and individual transaction details) with [Stripe Checkout](https://docs.stripe.com/tax/checkout.md):

#### Direct charges

For the Checkout Sessions API calls:

- Include the `Stripe-Account` header with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=2 \
  -d "automatic_tax[enabled]"=true \
  -d mode=payment \
  --data-urlencode success_url="https://example.com/success"
```

```cli
stripe checkout sessions create  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=2 \
  -d "automatic_tax[enabled]"=true \
  --mode=payment \
  --success-url="https://example.com/success"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create(
  {
    line_items: [
      {
        price: '{{PRICE_ID}}',
        quantity: 2,
      },
    ],
    automatic_tax: {enabled: true},
    mode: 'payment',
    success_url: 'https://example.com/success',
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create(
  {
    "line_items": [{"price": "{{PRICE_ID}}", "quantity": 2}],
    "automatic_tax": {"enabled": True},
    "mode": "payment",
    "success_url": "https://example.com/success",
  },
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create(
  [
    'line_items' => [
      [
        'price' => '{{PRICE_ID}}',
        'quantity' => 2,
      ],
    ],
    'automatic_tax' => ['enabled' => true],
    'mode' => 'payment',
    'success_url' => 'https://example.com/success',
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
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
    .setAutomaticTax(SessionCreateParams.AutomaticTax.builder().setEnabled(true).build())
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setSuccessUrl("https://example.com/success")
    .build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

Session session = client.v1().checkout().sessions().create(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create(
  {
    line_items: [
      {
        price: '{{PRICE_ID}}',
        quantity: 2,
      },
    ],
    automatic_tax: {
      enabled: true,
    },
    mode: 'payment',
    success_url: 'https://example.com/success',
  },
  {
    stripeAccount: '{{CONNECTEDACCOUNT_ID}}',
  }
);
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
  },
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  SuccessURL: stripe.String("https://example.com/success"),
}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
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
    AutomaticTax = new Stripe.Checkout.SessionAutomaticTaxOptions { Enabled = true },
    Mode = "payment",
    SuccessUrl = "https://example.com/success",
};
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options, requestOptions);
```

#### Destination charges

For the Checkout Sessions API calls:

- Include [automatic_tax[liability]](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-automatic_tax-liability) with `type=account` and `account` with the value of the connected account ID.
- Include [payment_intent_data[transfer_data][destination]](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-payment_intent_data-transfer_data-destination) with the value of the connected account ID.
- (Optional) If you’re [automatically sending invoices](https://docs.stripe.com/payments/checkout/receipts.md?payment-ui=stripe-hosted#automatically-send-receipts), include [invoice_creation[invoice_data][issuer]](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-invoice_creation-invoice_data-issuer) with `type=account` and `account` with the value of the connected account ID.
- (Optional) If the connected account is the [settlement merchant](https://docs.stripe.com/connect/destination-charges.md#settlement-merchant), include [payment_intent_data[on_behalf_of]](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-payment_intent_data-on_behalf_of) with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=2 \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=account \
  -d "automatic_tax[liability][account]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "payment_intent_data[transfer_data][destination]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "invoice_creation[enabled]"=true \
  -d "invoice_creation[invoice_data][issuer][type]"=account \
  -d "invoice_creation[invoice_data][issuer][account]"="{{CONNECTEDACCOUNT_ID}}" \
  -d mode=payment \
  --data-urlencode success_url="https://example.com/success"
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=2 \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=account \
  -d "automatic_tax[liability][account]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "payment_intent_data[transfer_data][destination]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "invoice_creation[enabled]"=true \
  -d "invoice_creation[invoice_data][issuer][type]"=account \
  -d "invoice_creation[invoice_data][issuer][account]"="{{CONNECTEDACCOUNT_ID}}" \
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
    liability: {
      type: 'account',
      account: '{{CONNECTEDACCOUNT_ID}}',
    },
  },
  payment_intent_data: {transfer_data: {destination: '{{CONNECTEDACCOUNT_ID}}'}},
  invoice_creation: {
    enabled: true,
    invoice_data: {
      issuer: {
        type: 'account',
        account: '{{CONNECTEDACCOUNT_ID}}',
      },
    },
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
  "automatic_tax": {
    "enabled": True,
    "liability": {"type": "account", "account": "{{CONNECTEDACCOUNT_ID}}"},
  },
  "payment_intent_data": {
    "transfer_data": {"destination": "{{CONNECTEDACCOUNT_ID}}"},
  },
  "invoice_creation": {
    "enabled": True,
    "invoice_data": {
      "issuer": {"type": "account", "account": "{{CONNECTEDACCOUNT_ID}}"},
    },
  },
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
    'liability' => [
      'type' => 'account',
      'account' => '{{CONNECTEDACCOUNT_ID}}',
    ],
  ],
  'payment_intent_data' => [
    'transfer_data' => ['destination' => '{{CONNECTEDACCOUNT_ID}}'],
  ],
  'invoice_creation' => [
    'enabled' => true,
    'invoice_data' => [
      'issuer' => [
        'type' => 'account',
        'account' => '{{CONNECTEDACCOUNT_ID}}',
      ],
    ],
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
            .setType(SessionCreateParams.AutomaticTax.Liability.Type.ACCOUNT)
            .setAccount("{{CONNECTEDACCOUNT_ID}}")
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
                .setType(
                  SessionCreateParams.InvoiceCreation.InvoiceData.Issuer.Type.ACCOUNT
                )
                .setAccount("{{CONNECTEDACCOUNT_ID}}")
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
      type: 'account',
      account: '{{CONNECTEDACCOUNT_ID}}',
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
        type: 'account',
        account: '{{CONNECTEDACCOUNT_ID}}',
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
      Type: stripe.String(stripe.CheckoutSessionAutomaticTaxLiabilityTypeAccount),
      Account: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
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
        Type: stripe.String(stripe.CheckoutSessionInvoiceCreationInvoiceDataIssuerTypeAccount),
        Account: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
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
            Type = "account",
            Account = "{{CONNECTEDACCOUNT_ID}}",
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
                Type = "account",
                Account = "{{CONNECTEDACCOUNT_ID}}",
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

- Include [automatic_tax[liability]](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-automatic_tax-liability) with `type=account` and `account` with the value of the connected account ID.
- (Optional) If you’re [automatically sending invoices](https://docs.stripe.com/payments/checkout/receipts.md?payment-ui=stripe-hosted#automatically-send-receipts), include [invoice_creation[invoice_data][issuer]](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-invoice_creation-invoice_data-issuer) with `type=account` and `account` with the value of the connected account ID.
- (Optional) If the connected account is the [settlement merchant](https://docs.stripe.com/connect/separate-charges-and-transfers.md#settlement-merchant), include [payment_intent_data[on_behalf_of]](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-payment_intent_data-on_behalf_of) with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=2 \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=account \
  -d "automatic_tax[liability][account]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "invoice_creation[enabled]"=true \
  -d "invoice_creation[invoice_data][issuer][type]"=account \
  -d "invoice_creation[invoice_data][issuer][account]"="{{CONNECTEDACCOUNT_ID}}" \
  -d mode=payment \
  --data-urlencode success_url="https://example.com/success"
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=2 \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=account \
  -d "automatic_tax[liability][account]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "invoice_creation[enabled]"=true \
  -d "invoice_creation[invoice_data][issuer][type]"=account \
  -d "invoice_creation[invoice_data][issuer][account]"="{{CONNECTEDACCOUNT_ID}}" \
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
    liability: {
      type: 'account',
      account: '{{CONNECTEDACCOUNT_ID}}',
    },
  },
  invoice_creation: {
    enabled: true,
    invoice_data: {
      issuer: {
        type: 'account',
        account: '{{CONNECTEDACCOUNT_ID}}',
      },
    },
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
  "automatic_tax": {
    "enabled": True,
    "liability": {"type": "account", "account": "{{CONNECTEDACCOUNT_ID}}"},
  },
  "invoice_creation": {
    "enabled": True,
    "invoice_data": {
      "issuer": {"type": "account", "account": "{{CONNECTEDACCOUNT_ID}}"},
    },
  },
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
    'liability' => [
      'type' => 'account',
      'account' => '{{CONNECTEDACCOUNT_ID}}',
    ],
  ],
  'invoice_creation' => [
    'enabled' => true,
    'invoice_data' => [
      'issuer' => [
        'type' => 'account',
        'account' => '{{CONNECTEDACCOUNT_ID}}',
      ],
    ],
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
            .setType(SessionCreateParams.AutomaticTax.Liability.Type.ACCOUNT)
            .setAccount("{{CONNECTEDACCOUNT_ID}}")
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
                .setType(
                  SessionCreateParams.InvoiceCreation.InvoiceData.Issuer.Type.ACCOUNT
                )
                .setAccount("{{CONNECTEDACCOUNT_ID}}")
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
      type: 'account',
      account: '{{CONNECTEDACCOUNT_ID}}',
    },
  },
  invoice_creation: {
    enabled: true,
    invoice_data: {
      issuer: {
        type: 'account',
        account: '{{CONNECTEDACCOUNT_ID}}',
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
      Type: stripe.String(stripe.CheckoutSessionAutomaticTaxLiabilityTypeAccount),
      Account: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
    },
  },
  InvoiceCreation: &stripe.CheckoutSessionCreateInvoiceCreationParams{
    Enabled: stripe.Bool(true),
    InvoiceData: &stripe.CheckoutSessionCreateInvoiceCreationInvoiceDataParams{
      Issuer: &stripe.CheckoutSessionCreateInvoiceCreationInvoiceDataIssuerParams{
        Type: stripe.String(stripe.CheckoutSessionInvoiceCreationInvoiceDataIssuerTypeAccount),
        Account: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
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
            Type = "account",
            Account = "{{CONNECTEDACCOUNT_ID}}",
        },
    },
    InvoiceCreation = new Stripe.Checkout.SessionInvoiceCreationOptions
    {
        Enabled = true,
        InvoiceData = new Stripe.Checkout.SessionInvoiceCreationInvoiceDataOptions
        {
            Issuer = new Stripe.Checkout.SessionInvoiceCreationInvoiceDataIssuerOptions
            {
                Type = "account",
                Account = "{{CONNECTEDACCOUNT_ID}}",
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

- Include [source_transaction](https://docs.stripe.com/api/transfers/create.md#create_transfer-source_transaction) to tie the transfer to the Payment Intent created by the Checkout Session.
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

Pick one of the currently supported [charge types](https://docs.stripe.com/connect/charges.md#types) that allow the connected account to be *liable for tax* (The responsibility for collecting and reporting taxes for transactions in a Connect integration. It can belong to the platform or to connected accounts, depending on your business model, government regulations, and individual transaction details) with [Stripe Checkout](https://docs.stripe.com/tax/checkout.md):

#### Direct charges

For the Checkout Sessions API calls:

- Include the `Stripe-Account` header with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "automatic_tax[enabled]"=true \
  -d mode=subscription \
  --data-urlencode success_url="https://example.com/success"
```

```cli
stripe checkout sessions create  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "automatic_tax[enabled]"=true \
  --mode=subscription \
  --success-url="https://example.com/success"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create(
  {
    line_items: [
      {
        price: '{{PRICE_ID}}',
        quantity: 1,
      },
    ],
    automatic_tax: {enabled: true},
    mode: 'subscription',
    success_url: 'https://example.com/success',
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create(
  {
    "line_items": [{"price": "{{PRICE_ID}}", "quantity": 1}],
    "automatic_tax": {"enabled": True},
    "mode": "subscription",
    "success_url": "https://example.com/success",
  },
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create(
  [
    'line_items' => [
      [
        'price' => '{{PRICE_ID}}',
        'quantity' => 1,
      ],
    ],
    'automatic_tax' => ['enabled' => true],
    'mode' => 'subscription',
    'success_url' => 'https://example.com/success',
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
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
    .setAutomaticTax(SessionCreateParams.AutomaticTax.builder().setEnabled(true).build())
    .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
    .setSuccessUrl("https://example.com/success")
    .build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

Session session = client.v1().checkout().sessions().create(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create(
  {
    line_items: [
      {
        price: '{{PRICE_ID}}',
        quantity: 1,
      },
    ],
    automatic_tax: {
      enabled: true,
    },
    mode: 'subscription',
    success_url: 'https://example.com/success',
  },
  {
    stripeAccount: '{{CONNECTEDACCOUNT_ID}}',
  }
);
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
  },
  Mode: stripe.String(stripe.CheckoutSessionModeSubscription),
  SuccessURL: stripe.String("https://example.com/success"),
}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
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
    AutomaticTax = new Stripe.Checkout.SessionAutomaticTaxOptions { Enabled = true },
    Mode = "subscription",
    SuccessUrl = "https://example.com/success",
};
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options, requestOptions);
```

#### Destination charges

For the Checkout Sessions API calls:

- Include [automatic_tax[liability]](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-automatic_tax-liability) with `type=account` and `account` with the value of the connected account ID.
- Include [subscription_data[transfer_data][destination]](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-subscription_data-transfer_data-destination) with the value of the connected account ID.
- Include [subscription_data[invoice_settings][issuer]](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-subscription_data-invoice_settings-issuer) with `type=account` and `account` with the value of the connected account ID.
- (Optional) If the connected account is the [settlement merchant](https://docs.stripe.com/connect/destination-charges.md#settlement-merchant), include [subscription_data[on_behalf_of]](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-subscription_data-on_behalf_of) with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=account \
  -d "automatic_tax[liability][account]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "subscription_data[transfer_data][destination]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "subscription_data[invoice_settings][issuer][type]"=account \
  -d "subscription_data[invoice_settings][issuer][account]"="{{CONNECTEDACCOUNT_ID}}" \
  -d mode=subscription \
  --data-urlencode success_url="https://example.com/success"
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=account \
  -d "automatic_tax[liability][account]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "subscription_data[transfer_data][destination]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "subscription_data[invoice_settings][issuer][type]"=account \
  -d "subscription_data[invoice_settings][issuer][account]"="{{CONNECTEDACCOUNT_ID}}" \
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
    liability: {
      type: 'account',
      account: '{{CONNECTEDACCOUNT_ID}}',
    },
  },
  subscription_data: {
    transfer_data: {destination: '{{CONNECTEDACCOUNT_ID}}'},
    invoice_settings: {
      issuer: {
        type: 'account',
        account: '{{CONNECTEDACCOUNT_ID}}',
      },
    },
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
  "automatic_tax": {
    "enabled": True,
    "liability": {"type": "account", "account": "{{CONNECTEDACCOUNT_ID}}"},
  },
  "subscription_data": {
    "transfer_data": {"destination": "{{CONNECTEDACCOUNT_ID}}"},
    "invoice_settings": {
      "issuer": {"type": "account", "account": "{{CONNECTEDACCOUNT_ID}}"},
    },
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
    'liability' => [
      'type' => 'account',
      'account' => '{{CONNECTEDACCOUNT_ID}}',
    ],
  ],
  'subscription_data' => [
    'transfer_data' => ['destination' => '{{CONNECTEDACCOUNT_ID}}'],
    'invoice_settings' => [
      'issuer' => [
        'type' => 'account',
        'account' => '{{CONNECTEDACCOUNT_ID}}',
      ],
    ],
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
            .setType(SessionCreateParams.AutomaticTax.Liability.Type.ACCOUNT)
            .setAccount("{{CONNECTEDACCOUNT_ID}}")
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
                  SessionCreateParams.SubscriptionData.InvoiceSettings.Issuer.Type.ACCOUNT
                )
                .setAccount("{{CONNECTEDACCOUNT_ID}}")
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
      type: 'account',
      account: '{{CONNECTEDACCOUNT_ID}}',
    },
  },
  subscription_data: {
    transfer_data: {
      destination: '{{CONNECTEDACCOUNT_ID}}',
    },
    invoice_settings: {
      issuer: {
        type: 'account',
        account: '{{CONNECTEDACCOUNT_ID}}',
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
      Type: stripe.String(stripe.CheckoutSessionAutomaticTaxLiabilityTypeAccount),
      Account: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
    },
  },
  SubscriptionData: &stripe.CheckoutSessionCreateSubscriptionDataParams{
    TransferData: &stripe.CheckoutSessionCreateSubscriptionDataTransferDataParams{
      Destination: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
    },
    InvoiceSettings: &stripe.CheckoutSessionCreateSubscriptionDataInvoiceSettingsParams{
      Issuer: &stripe.CheckoutSessionCreateSubscriptionDataInvoiceSettingsIssuerParams{
        Type: stripe.String("account"),
        Account: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
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
            Type = "account",
            Account = "{{CONNECTEDACCOUNT_ID}}",
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
                Type = "account",
                Account = "{{CONNECTEDACCOUNT_ID}}",
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

- Include [automatic_tax[liability]](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-automatic_tax-liability) with `type=account` and `account` with the value of the connected account ID.
- Include [subscription_data[invoice_settings][issuer]](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-subscription_data-invoice_settings-issuer) with `type=account` and `account` with the value of the connected account ID.
- (Optional) If the connected account is the [settlement merchant](https://docs.stripe.com/connect/separate-charges-and-transfers.md#settlement-merchant), include [subscription_data[on_behalf_of]](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-subscription_data-on_behalf_of) with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=account \
  -d "automatic_tax[liability][account]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "subscription_data[invoice_settings][issuer][type]"=account \
  -d "subscription_data[invoice_settings][issuer][account]"="{{CONNECTEDACCOUNT_ID}}" \
  -d mode=subscription \
  --data-urlencode success_url="https://example.com/success"
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=account \
  -d "automatic_tax[liability][account]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "subscription_data[invoice_settings][issuer][type]"=account \
  -d "subscription_data[invoice_settings][issuer][account]"="{{CONNECTEDACCOUNT_ID}}" \
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
    liability: {
      type: 'account',
      account: '{{CONNECTEDACCOUNT_ID}}',
    },
  },
  subscription_data: {
    invoice_settings: {
      issuer: {
        type: 'account',
        account: '{{CONNECTEDACCOUNT_ID}}',
      },
    },
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
  "automatic_tax": {
    "enabled": True,
    "liability": {"type": "account", "account": "{{CONNECTEDACCOUNT_ID}}"},
  },
  "subscription_data": {
    "invoice_settings": {
      "issuer": {"type": "account", "account": "{{CONNECTEDACCOUNT_ID}}"},
    },
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
    'liability' => [
      'type' => 'account',
      'account' => '{{CONNECTEDACCOUNT_ID}}',
    ],
  ],
  'subscription_data' => [
    'invoice_settings' => [
      'issuer' => [
        'type' => 'account',
        'account' => '{{CONNECTEDACCOUNT_ID}}',
      ],
    ],
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
            .setType(SessionCreateParams.AutomaticTax.Liability.Type.ACCOUNT)
            .setAccount("{{CONNECTEDACCOUNT_ID}}")
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
                  SessionCreateParams.SubscriptionData.InvoiceSettings.Issuer.Type.ACCOUNT
                )
                .setAccount("{{CONNECTEDACCOUNT_ID}}")
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
      type: 'account',
      account: '{{CONNECTEDACCOUNT_ID}}',
    },
  },
  subscription_data: {
    invoice_settings: {
      issuer: {
        type: 'account',
        account: '{{CONNECTEDACCOUNT_ID}}',
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
      Type: stripe.String(stripe.CheckoutSessionAutomaticTaxLiabilityTypeAccount),
      Account: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
    },
  },
  SubscriptionData: &stripe.CheckoutSessionCreateSubscriptionDataParams{
    InvoiceSettings: &stripe.CheckoutSessionCreateSubscriptionDataInvoiceSettingsParams{
      Issuer: &stripe.CheckoutSessionCreateSubscriptionDataInvoiceSettingsIssuerParams{
        Type: stripe.String("account"),
        Account: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
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
            Type = "account",
            Account = "{{CONNECTEDACCOUNT_ID}}",
        },
    },
    SubscriptionData = new Stripe.Checkout.SessionSubscriptionDataOptions
    {
        InvoiceSettings = new Stripe.Checkout.SessionSubscriptionDataInvoiceSettingsOptions
        {
            Issuer = new Stripe.Checkout.SessionSubscriptionDataInvoiceSettingsIssuerOptions
            {
                Type = "account",
                Account = "{{CONNECTEDACCOUNT_ID}}",
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

- Include [source_transaction](https://docs.stripe.com/api/transfers/create.md#create_transfer-source_transaction) to tie the transfer to the Payment Intent created by the Checkout Session.
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

Pick one of the currently supported [charge types](https://docs.stripe.com/connect/charges.md#types) that allow the connected account to be *liable for tax* (The responsibility for collecting and reporting taxes for transactions in a Connect integration. It can belong to the platform or to connected accounts, depending on your business model, government regulations, and individual transaction details) with [Stripe Subscriptions](https://docs.stripe.com/tax/subscriptions.md):

#### Direct charges

For the Subscriptions API calls:

- Include the `Stripe-Account` header with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/subscriptions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d "items[0][price]"="{{PRICE_ID}}" \
  -d "items[0][quantity]"=1 \
  -d customer="{{CUSTOMER_ID}}" \
  -d "automatic_tax[enabled]"=true
```

```cli
stripe subscriptions create  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  -d "items[0][price]"="{{PRICE_ID}}" \
  -d "items[0][quantity]"=1 \
  --customer="{{CUSTOMER_ID}}" \
  -d "automatic_tax[enabled]"=true
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

subscription = client.v1.subscriptions.create(
  {
    items: [
      {
        price: '{{PRICE_ID}}',
        quantity: 1,
      },
    ],
    customer: '{{CUSTOMER_ID}}',
    automatic_tax: {enabled: true},
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
subscription = client.v1.subscriptions.create(
  {
    "items": [{"price": "{{PRICE_ID}}", "quantity": 1}],
    "customer": "{{CUSTOMER_ID}}",
    "automatic_tax": {"enabled": True},
  },
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$subscription = $stripe->subscriptions->create(
  [
    'items' => [
      [
        'price' => '{{PRICE_ID}}',
        'quantity' => 1,
      ],
    ],
    'customer' => '{{CUSTOMER_ID}}',
    'automatic_tax' => ['enabled' => true],
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
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
      SubscriptionCreateParams.AutomaticTax.builder().setEnabled(true).build()
    )
    .build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

Subscription subscription = client.v1().subscriptions().create(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const subscription = await stripe.subscriptions.create(
  {
    items: [
      {
        price: '{{PRICE_ID}}',
        quantity: 1,
      },
    ],
    customer: '{{CUSTOMER_ID}}',
    automatic_tax: {
      enabled: true,
    },
  },
  {
    stripeAccount: '{{CONNECTEDACCOUNT_ID}}',
  }
);
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
  AutomaticTax: &stripe.SubscriptionCreateAutomaticTaxParams{Enabled: stripe.Bool(true)},
}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
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
    AutomaticTax = new SubscriptionAutomaticTaxOptions { Enabled = true },
};
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Subscriptions;
Subscription subscription = service.Create(options, requestOptions);
```

#### Destination charges

For the Subscriptions API calls:

- Include [automatic_tax[liability]](https://docs.stripe.com/api/subscriptions/create.md#create_subscription-automatic_tax-liability) with `type=account` and `account` with the value of the connected account ID.
- Include [transfer_data[destination]](https://docs.stripe.com/api/subscriptions/create.md#create_subscription-transfer_data-destination) with the value of the connected account ID.
- Include [invoice_settings[issuer]](https://docs.stripe.com/api/subscriptions/create.md#create_subscription-invoice_settings-issuer) with `type=account` and `account` with the value of the connected account ID. In some jurisdictions, like the European Union, invoice PDFs are used as the tax instrument and the invoice issuer must match the entity *liable for tax* (The responsibility for collecting and reporting taxes for transactions in a Connect integration. It can belong to the platform or to connected accounts, depending on your business model, government regulations, and individual transaction details) at all times.
- (Optional) If the connected account is the [settlement merchant](https://docs.stripe.com/connect/destination-charges.md#settlement-merchant), include [on_behalf_of](https://docs.stripe.com/api/subscriptions/create.md#create_subscription-on_behalf_of) with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/subscriptions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "items[0][price]"="{{PRICE_ID}}" \
  -d "items[0][quantity]"=1 \
  -d customer="{{CUSTOMER_ID}}" \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=account \
  -d "automatic_tax[liability][account]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "transfer_data[destination]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "invoice_settings[issuer][type]"=account \
  -d "invoice_settings[issuer][account]"="{{CONNECTEDACCOUNT_ID}}"
```

```cli
stripe subscriptions create  \
  -d "items[0][price]"="{{PRICE_ID}}" \
  -d "items[0][quantity]"=1 \
  --customer="{{CUSTOMER_ID}}" \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=account \
  -d "automatic_tax[liability][account]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "transfer_data[destination]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "invoice_settings[issuer][type]"=account \
  -d "invoice_settings[issuer][account]"="{{CONNECTEDACCOUNT_ID}}"
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
    liability: {
      type: 'account',
      account: '{{CONNECTEDACCOUNT_ID}}',
    },
  },
  transfer_data: {destination: '{{CONNECTEDACCOUNT_ID}}'},
  invoice_settings: {
    issuer: {
      type: 'account',
      account: '{{CONNECTEDACCOUNT_ID}}',
    },
  },
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
  "automatic_tax": {
    "enabled": True,
    "liability": {"type": "account", "account": "{{CONNECTEDACCOUNT_ID}}"},
  },
  "transfer_data": {"destination": "{{CONNECTEDACCOUNT_ID}}"},
  "invoice_settings": {
    "issuer": {"type": "account", "account": "{{CONNECTEDACCOUNT_ID}}"},
  },
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
    'liability' => [
      'type' => 'account',
      'account' => '{{CONNECTEDACCOUNT_ID}}',
    ],
  ],
  'transfer_data' => ['destination' => '{{CONNECTEDACCOUNT_ID}}'],
  'invoice_settings' => [
    'issuer' => [
      'type' => 'account',
      'account' => '{{CONNECTEDACCOUNT_ID}}',
    ],
  ],
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
            .setType(SubscriptionCreateParams.AutomaticTax.Liability.Type.ACCOUNT)
            .setAccount("{{CONNECTEDACCOUNT_ID}}")
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
            .setType(SubscriptionCreateParams.InvoiceSettings.Issuer.Type.ACCOUNT)
            .setAccount("{{CONNECTEDACCOUNT_ID}}")
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
      type: 'account',
      account: '{{CONNECTEDACCOUNT_ID}}',
    },
  },
  transfer_data: {
    destination: '{{CONNECTEDACCOUNT_ID}}',
  },
  invoice_settings: {
    issuer: {
      type: 'account',
      account: '{{CONNECTEDACCOUNT_ID}}',
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
      Type: stripe.String(stripe.SubscriptionAutomaticTaxLiabilityTypeAccount),
      Account: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
    },
  },
  TransferData: &stripe.SubscriptionCreateTransferDataParams{
    Destination: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
  },
  InvoiceSettings: &stripe.SubscriptionCreateInvoiceSettingsParams{
    Issuer: &stripe.SubscriptionCreateInvoiceSettingsIssuerParams{
      Type: stripe.String(stripe.SubscriptionInvoiceSettingsIssuerTypeAccount),
      Account: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
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
        Liability = new SubscriptionAutomaticTaxLiabilityOptions
        {
            Type = "account",
            Account = "{{CONNECTEDACCOUNT_ID}}",
        },
    },
    TransferData = new SubscriptionTransferDataOptions
    {
        Destination = "{{CONNECTEDACCOUNT_ID}}",
    },
    InvoiceSettings = new SubscriptionInvoiceSettingsOptions
    {
        Issuer = new SubscriptionInvoiceSettingsIssuerOptions
        {
            Type = "account",
            Account = "{{CONNECTEDACCOUNT_ID}}",
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Subscriptions;
Subscription subscription = service.Create(options);
```

#### Separate charges and transfers

For the Subscriptions API calls:

- Include [automatic_tax[liability]](https://docs.stripe.com/api/subscriptions/create.md#create_subscription-automatic_tax-liability) with `type=account` and `account` with the value of the connected account ID.
- Include [invoice_settings[issuer]](https://docs.stripe.com/api/subscriptions/create.md#create_subscription-invoice_settings-issuer) with `type=account` and `account` with the value of the connected account ID. In some jurisdictions, like the European Union, invoice PDFs are used as the tax instrument and the invoice issuer must match the entity *liable for tax* (The responsibility for collecting and reporting taxes for transactions in a Connect integration. It can belong to the platform or to connected accounts, depending on your business model, government regulations, and individual transaction details) at all times.
- Include [on_behalf_of](https://docs.stripe.com/api/subscriptions/create.md#create_subscription-on_behalf_of) with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/subscriptions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "items[0][price]"="{{PRICE_ID}}" \
  -d "items[0][quantity]"=1 \
  -d customer="{{CUSTOMER_ID}}" \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=account \
  -d "automatic_tax[liability][account]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "invoice_settings[issuer][type]"=account \
  -d "invoice_settings[issuer][account]"="{{CONNECTEDACCOUNT_ID}}" \
  -d on_behalf_of="{{CONNECTEDACCOUNT_ID}}"
```

```cli
stripe subscriptions create  \
  -d "items[0][price]"="{{PRICE_ID}}" \
  -d "items[0][quantity]"=1 \
  --customer="{{CUSTOMER_ID}}" \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=account \
  -d "automatic_tax[liability][account]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "invoice_settings[issuer][type]"=account \
  -d "invoice_settings[issuer][account]"="{{CONNECTEDACCOUNT_ID}}" \
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
    liability: {
      type: 'account',
      account: '{{CONNECTEDACCOUNT_ID}}',
    },
  },
  invoice_settings: {
    issuer: {
      type: 'account',
      account: '{{CONNECTEDACCOUNT_ID}}',
    },
  },
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
  "automatic_tax": {
    "enabled": True,
    "liability": {"type": "account", "account": "{{CONNECTEDACCOUNT_ID}}"},
  },
  "invoice_settings": {
    "issuer": {"type": "account", "account": "{{CONNECTEDACCOUNT_ID}}"},
  },
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
    'liability' => [
      'type' => 'account',
      'account' => '{{CONNECTEDACCOUNT_ID}}',
    ],
  ],
  'invoice_settings' => [
    'issuer' => [
      'type' => 'account',
      'account' => '{{CONNECTEDACCOUNT_ID}}',
    ],
  ],
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
            .setType(SubscriptionCreateParams.AutomaticTax.Liability.Type.ACCOUNT)
            .setAccount("{{CONNECTEDACCOUNT_ID}}")
            .build()
        )
        .build()
    )
    .setInvoiceSettings(
      SubscriptionCreateParams.InvoiceSettings.builder()
        .setIssuer(
          SubscriptionCreateParams.InvoiceSettings.Issuer.builder()
            .setType(SubscriptionCreateParams.InvoiceSettings.Issuer.Type.ACCOUNT)
            .setAccount("{{CONNECTEDACCOUNT_ID}}")
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
      type: 'account',
      account: '{{CONNECTEDACCOUNT_ID}}',
    },
  },
  invoice_settings: {
    issuer: {
      type: 'account',
      account: '{{CONNECTEDACCOUNT_ID}}',
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
      Type: stripe.String(stripe.SubscriptionAutomaticTaxLiabilityTypeAccount),
      Account: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
    },
  },
  InvoiceSettings: &stripe.SubscriptionCreateInvoiceSettingsParams{
    Issuer: &stripe.SubscriptionCreateInvoiceSettingsIssuerParams{
      Type: stripe.String(stripe.SubscriptionInvoiceSettingsIssuerTypeAccount),
      Account: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
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
        Liability = new SubscriptionAutomaticTaxLiabilityOptions
        {
            Type = "account",
            Account = "{{CONNECTEDACCOUNT_ID}}",
        },
    },
    InvoiceSettings = new SubscriptionInvoiceSettingsOptions
    {
        Issuer = new SubscriptionInvoiceSettingsIssuerOptions
        {
            Type = "account",
            Account = "{{CONNECTEDACCOUNT_ID}}",
        },
    },
    OnBehalfOf = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Subscriptions;
Subscription subscription = service.Create(options);
```

For the Transfers API calls:

- Include [source_transaction](https://docs.stripe.com/api/transfers/create.md#create_transfer-source_transaction) to tie the transfer to the payment intent created by the subscription invoice.
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

Pick one of the currently supported [charge types](https://docs.stripe.com/connect/charges.md#types) that allow the connected account to be *liable for tax* (The responsibility for collecting and reporting taxes for transactions in a Connect integration. It can belong to the platform or to connected accounts, depending on your business model, government regulations, and individual transaction details) with [Stripe Invoicing](https://docs.stripe.com/tax/invoicing.md):

#### Direct charges

For the Invoices API calls:

- Include the `Stripe-Account` header with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/invoices \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d customer="{{CUSTOMER_ID}}" \
  -d "automatic_tax[enabled]"=true
```

```cli
stripe invoices create  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  --customer="{{CUSTOMER_ID}}" \
  -d "automatic_tax[enabled]"=true
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

invoice = client.v1.invoices.create(
  {
    customer: '{{CUSTOMER_ID}}',
    automatic_tax: {enabled: true},
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
invoice = client.v1.invoices.create(
  {"customer": "{{CUSTOMER_ID}}", "automatic_tax": {"enabled": True}},
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$invoice = $stripe->invoices->create(
  [
    'customer' => '{{CUSTOMER_ID}}',
    'automatic_tax' => ['enabled' => true],
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

InvoiceCreateParams params =
  InvoiceCreateParams.builder()
    .setCustomer("{{CUSTOMER_ID}}")
    .setAutomaticTax(InvoiceCreateParams.AutomaticTax.builder().setEnabled(true).build())
    .build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

Invoice invoice = client.v1().invoices().create(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const invoice = await stripe.invoices.create(
  {
    customer: '{{CUSTOMER_ID}}',
    automatic_tax: {
      enabled: true,
    },
  },
  {
    stripeAccount: '{{CONNECTEDACCOUNT_ID}}',
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.InvoiceCreateParams{
  Customer: stripe.String("{{CUSTOMER_ID}}"),
  AutomaticTax: &stripe.InvoiceCreateAutomaticTaxParams{Enabled: stripe.Bool(true)},
}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result, err := sc.V1Invoices.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new InvoiceCreateOptions
{
    Customer = "{{CUSTOMER_ID}}",
    AutomaticTax = new InvoiceAutomaticTaxOptions { Enabled = true },
};
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Invoices;
Invoice invoice = service.Create(options, requestOptions);
```

#### Destination charges

For the Invoices API calls:

- Include [automatic_tax[liability]](https://docs.stripe.com/api/invoices/create.md#create_invoice-automatic_tax-liability) with `type=account` and `account` with the value of the connected account ID.
- Include [transfer_data[destination]](https://docs.stripe.com/api/invoices/create.md#create_invoice-transfer_data-destination) with the value of the connected account ID.
- Include [issuer](https://docs.stripe.com/api/invoices/create.md#create_invoice-issuer) with `type=account` and `account` with the value of the connected account ID. In some jurisdictions, like the European Union, invoice PDFs are used as the tax instrument and the invoice issuer must match the entity *liable for tax* (The responsibility for collecting and reporting taxes for transactions in a Connect integration. It can belong to the platform or to connected accounts, depending on your business model, government regulations, and individual transaction details) at all times.
- (Optional) If the connected account is the [settlement merchant](https://docs.stripe.com/connect/destination-charges.md#settlement-merchant), include [on_behalf_of](https://docs.stripe.com/api/invoices/create.md#create_invoice-on_behalf_of) with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/invoices \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer="{{CUSTOMER_ID}}" \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=account \
  -d "automatic_tax[liability][account]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "transfer_data[destination]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "issuer[type]"=account \
  -d "issuer[account]"="{{CONNECTEDACCOUNT_ID}}"
```

```cli
stripe invoices create  \
  --customer="{{CUSTOMER_ID}}" \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=account \
  -d "automatic_tax[liability][account]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "transfer_data[destination]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "issuer[type]"=account \
  -d "issuer[account]"="{{CONNECTEDACCOUNT_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

invoice = client.v1.invoices.create({
  customer: '{{CUSTOMER_ID}}',
  automatic_tax: {
    enabled: true,
    liability: {
      type: 'account',
      account: '{{CONNECTEDACCOUNT_ID}}',
    },
  },
  transfer_data: {destination: '{{CONNECTEDACCOUNT_ID}}'},
  issuer: {
    type: 'account',
    account: '{{CONNECTEDACCOUNT_ID}}',
  },
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
invoice = client.v1.invoices.create({
  "customer": "{{CUSTOMER_ID}}",
  "automatic_tax": {
    "enabled": True,
    "liability": {"type": "account", "account": "{{CONNECTEDACCOUNT_ID}}"},
  },
  "transfer_data": {"destination": "{{CONNECTEDACCOUNT_ID}}"},
  "issuer": {"type": "account", "account": "{{CONNECTEDACCOUNT_ID}}"},
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
    'liability' => [
      'type' => 'account',
      'account' => '{{CONNECTEDACCOUNT_ID}}',
    ],
  ],
  'transfer_data' => ['destination' => '{{CONNECTEDACCOUNT_ID}}'],
  'issuer' => [
    'type' => 'account',
    'account' => '{{CONNECTEDACCOUNT_ID}}',
  ],
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
            .setType(InvoiceCreateParams.AutomaticTax.Liability.Type.ACCOUNT)
            .setAccount("{{CONNECTEDACCOUNT_ID}}")
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
        .setType(InvoiceCreateParams.Issuer.Type.ACCOUNT)
        .setAccount("{{CONNECTEDACCOUNT_ID}}")
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
      type: 'account',
      account: '{{CONNECTEDACCOUNT_ID}}',
    },
  },
  transfer_data: {
    destination: '{{CONNECTEDACCOUNT_ID}}',
  },
  issuer: {
    type: 'account',
    account: '{{CONNECTEDACCOUNT_ID}}',
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
      Type: stripe.String(stripe.InvoiceAutomaticTaxLiabilityTypeAccount),
      Account: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
    },
  },
  TransferData: &stripe.InvoiceCreateTransferDataParams{
    Destination: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
  },
  Issuer: &stripe.InvoiceCreateIssuerParams{
    Type: stripe.String(stripe.InvoiceIssuerTypeAccount),
    Account: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
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
        Liability = new InvoiceAutomaticTaxLiabilityOptions
        {
            Type = "account",
            Account = "{{CONNECTEDACCOUNT_ID}}",
        },
    },
    TransferData = new InvoiceTransferDataOptions
    {
        Destination = "{{CONNECTEDACCOUNT_ID}}",
    },
    Issuer = new InvoiceIssuerOptions
    {
        Type = "account",
        Account = "{{CONNECTEDACCOUNT_ID}}",
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Invoices;
Invoice invoice = service.Create(options);
```

#### Separate charges and transfers

For the Invoices API calls:

- Include [automatic_tax[liability]](https://docs.stripe.com/api/invoices/create.md#create_invoice-automatic_tax-liability) with `type=account` and `account` with the value of the connected account ID.
- Include [issuer](https://docs.stripe.com/api/invoices/create.md#create_invoice-issuer) with `type=account` and `account` with the value of the connected account ID. In some jurisdictions, like the European Union, invoice PDFs are used as the tax instrument and the invoice issuer must match the entity *liable for tax* (The responsibility for collecting and reporting taxes for transactions in a Connect integration. It can belong to the platform or to connected accounts, depending on your business model, government regulations, and individual transaction details) at all times.
- (Optional) If the connected account is the [settlement merchant](https://docs.stripe.com/connect/separate-charges-and-transfers.md#settlement-merchant), include [on_behalf_of](https://docs.stripe.com/api/invoices/create.md#create_invoice-on_behalf_of) with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/invoices \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer="{{CUSTOMER_ID}}" \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=account \
  -d "automatic_tax[liability][account]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "issuer[type]"=account \
  -d "issuer[account]"="{{CONNECTEDACCOUNT_ID}}"
```

```cli
stripe invoices create  \
  --customer="{{CUSTOMER_ID}}" \
  -d "automatic_tax[enabled]"=true \
  -d "automatic_tax[liability][type]"=account \
  -d "automatic_tax[liability][account]"="{{CONNECTEDACCOUNT_ID}}" \
  -d "issuer[type]"=account \
  -d "issuer[account]"="{{CONNECTEDACCOUNT_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

invoice = client.v1.invoices.create({
  customer: '{{CUSTOMER_ID}}',
  automatic_tax: {
    enabled: true,
    liability: {
      type: 'account',
      account: '{{CONNECTEDACCOUNT_ID}}',
    },
  },
  issuer: {
    type: 'account',
    account: '{{CONNECTEDACCOUNT_ID}}',
  },
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
invoice = client.v1.invoices.create({
  "customer": "{{CUSTOMER_ID}}",
  "automatic_tax": {
    "enabled": True,
    "liability": {"type": "account", "account": "{{CONNECTEDACCOUNT_ID}}"},
  },
  "issuer": {"type": "account", "account": "{{CONNECTEDACCOUNT_ID}}"},
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
    'liability' => [
      'type' => 'account',
      'account' => '{{CONNECTEDACCOUNT_ID}}',
    ],
  ],
  'issuer' => [
    'type' => 'account',
    'account' => '{{CONNECTEDACCOUNT_ID}}',
  ],
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
            .setType(InvoiceCreateParams.AutomaticTax.Liability.Type.ACCOUNT)
            .setAccount("{{CONNECTEDACCOUNT_ID}}")
            .build()
        )
        .build()
    )
    .setIssuer(
      InvoiceCreateParams.Issuer.builder()
        .setType(InvoiceCreateParams.Issuer.Type.ACCOUNT)
        .setAccount("{{CONNECTEDACCOUNT_ID}}")
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
      type: 'account',
      account: '{{CONNECTEDACCOUNT_ID}}',
    },
  },
  issuer: {
    type: 'account',
    account: '{{CONNECTEDACCOUNT_ID}}',
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
      Type: stripe.String(stripe.InvoiceAutomaticTaxLiabilityTypeAccount),
      Account: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
    },
  },
  Issuer: &stripe.InvoiceCreateIssuerParams{
    Type: stripe.String(stripe.InvoiceIssuerTypeAccount),
    Account: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
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
        Liability = new InvoiceAutomaticTaxLiabilityOptions
        {
            Type = "account",
            Account = "{{CONNECTEDACCOUNT_ID}}",
        },
    },
    Issuer = new InvoiceIssuerOptions
    {
        Type = "account",
        Account = "{{CONNECTEDACCOUNT_ID}}",
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Invoices;
Invoice invoice = service.Create(options);
```

For the Transfers API calls:

- Include [source_transaction](https://docs.stripe.com/api/transfers/create.md#create_transfer-source_transaction) to tie the transfer to the payment intent created by the subscription invoice.
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

### Payment Intents

Pick one of the currently supported [charge types](https://docs.stripe.com/connect/charges.md#types) that allow the connected account to be *liable for tax* (The responsibility for collecting and reporting taxes for transactions in a Connect integration. It can belong to the platform or to connected accounts, depending on your business model, government regulations, and individual transaction details) with [Stripe Tax API](https://docs.stripe.com/tax/custom.md):

#### Direct charges

For the Tax Calculation API calls:

- Include the `Stripe-Account` header with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/tax/calculations \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d currency=usd \
  -d "line_items[0][amount]"=1000 \
  -d "line_items[0][reference]"=L1 \
  -d customer="{{CUSTOMER_ID}}"
```

```cli
stripe tax calculations create  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  --currency=usd \
  -d "line_items[0][amount]"=1000 \
  -d "line_items[0][reference]"=L1 \
  --customer="{{CUSTOMER_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

calculation = client.v1.tax.calculations.create(
  {
    currency: 'usd',
    line_items: [
      {
        amount: 1000,
        reference: 'L1',
      },
    ],
    customer: '{{CUSTOMER_ID}}',
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
calculation = client.v1.tax.calculations.create(
  {
    "currency": "usd",
    "line_items": [{"amount": 1000, "reference": "L1"}],
    "customer": "{{CUSTOMER_ID}}",
  },
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$calculation = $stripe->tax->calculations->create(
  [
    'currency' => 'usd',
    'line_items' => [
      [
        'amount' => 1000,
        'reference' => 'L1',
      ],
    ],
    'customer' => '{{CUSTOMER_ID}}',
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
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

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

Calculation calculation = client.v1().tax().calculations().create(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const calculation = await stripe.tax.calculations.create(
  {
    currency: 'usd',
    line_items: [
      {
        amount: 1000,
        reference: 'L1',
      },
    ],
    customer: '{{CUSTOMER_ID}}',
  },
  {
    stripeAccount: '{{CONNECTEDACCOUNT_ID}}',
  }
);
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
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
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
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Tax.Calculations;
Stripe.Tax.Calculation calculation = service.Create(options, requestOptions);
```

For the Payment Intents API calls:

- Include the `Stripe-Account` header with the value of the connected account ID.
- Include [amount](https://docs.stripe.com/api/payment_intents/create.md#create_payment_intent-amount) with the `amount_total` returned by the tax calculation.
- Include [metadata[tax_calculation]](https://docs.stripe.com/api/payment_intents/create.md#create_payment_intent-metadata) with the `id` returned by the tax calculation.

```curl
curl https://api.stripe.com/v1/payment_intents \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d amount=1000 \
  -d currency=usd \
  -d customer="{{CUSTOMER_ID}}" \
  -d "metadata[tax_calculation]"="{{TAXCALCULATION_ID}}"
```

```cli
stripe payment_intents create  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  --amount=1000 \
  --currency=usd \
  --customer="{{CUSTOMER_ID}}" \
  -d "metadata[tax_calculation]"="{{TAXCALCULATION_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

payment_intent = client.v1.payment_intents.create(
  {
    amount: 1000,
    currency: 'usd',
    customer: '{{CUSTOMER_ID}}',
    metadata: {tax_calculation: '{{TAXCALCULATION_ID}}'},
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
payment_intent = client.v1.payment_intents.create(
  {
    "amount": 1000,
    "currency": "usd",
    "customer": "{{CUSTOMER_ID}}",
    "metadata": {"tax_calculation": "{{TAXCALCULATION_ID}}"},
  },
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$paymentIntent = $stripe->paymentIntents->create(
  [
    'amount' => 1000,
    'currency' => 'usd',
    'customer' => '{{CUSTOMER_ID}}',
    'metadata' => ['tax_calculation' => '{{TAXCALCULATION_ID}}'],
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
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

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

PaymentIntent paymentIntent = client.v1().paymentIntents().create(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const paymentIntent = await stripe.paymentIntents.create(
  {
    amount: 1000,
    currency: 'usd',
    customer: '{{CUSTOMER_ID}}',
    metadata: {
      tax_calculation: '{{TAXCALCULATION_ID}}',
    },
  },
  {
    stripeAccount: '{{CONNECTEDACCOUNT_ID}}',
  }
);
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
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
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
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.PaymentIntents;
PaymentIntent paymentIntent = service.Create(options, requestOptions);
```

#### Destination charges

For the Tax Calculation API calls:

- Include the `Stripe-Account` header with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/tax/calculations \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d currency=usd \
  -d "line_items[0][amount]"=1000 \
  -d "line_items[0][reference]"=L1 \
  -d customer="{{CUSTOMER_ID}}"
```

```cli
stripe tax calculations create  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  --currency=usd \
  -d "line_items[0][amount]"=1000 \
  -d "line_items[0][reference]"=L1 \
  --customer="{{CUSTOMER_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

calculation = client.v1.tax.calculations.create(
  {
    currency: 'usd',
    line_items: [
      {
        amount: 1000,
        reference: 'L1',
      },
    ],
    customer: '{{CUSTOMER_ID}}',
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
calculation = client.v1.tax.calculations.create(
  {
    "currency": "usd",
    "line_items": [{"amount": 1000, "reference": "L1"}],
    "customer": "{{CUSTOMER_ID}}",
  },
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$calculation = $stripe->tax->calculations->create(
  [
    'currency' => 'usd',
    'line_items' => [
      [
        'amount' => 1000,
        'reference' => 'L1',
      ],
    ],
    'customer' => '{{CUSTOMER_ID}}',
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
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

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

Calculation calculation = client.v1().tax().calculations().create(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const calculation = await stripe.tax.calculations.create(
  {
    currency: 'usd',
    line_items: [
      {
        amount: 1000,
        reference: 'L1',
      },
    ],
    customer: '{{CUSTOMER_ID}}',
  },
  {
    stripeAccount: '{{CONNECTEDACCOUNT_ID}}',
  }
);
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
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
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
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Tax.Calculations;
Stripe.Tax.Calculation calculation = service.Create(options, requestOptions);
```

For the Payment Intents API calls:

- Include [amount](https://docs.stripe.com/api/payment_intents/create.md#create_payment_intent-amount) with the `amount_total` returned by the tax calculation.
- Include [metadata[tax_calculation]](https://docs.stripe.com/api/payment_intents/create.md#create_payment_intent-metadata) with the `id` returned by the tax calculation.
- Include [transfer_data[destination]](https://docs.stripe.com/api/payment_intents/create.md#create_payment_intent-transfer_data-destination) with the value of the connected account ID.
- You can use this regardless of the [settlement merchant](https://docs.stripe.com/connect/destination-charges.md#settlement-merchant) as specified by the [on_behalf_of](https://docs.stripe.com/api/payment_intents/create.md#create_payment_intent-on_behalf_of) parameter.

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

In this scenario, the connected account is liable for tax on the transaction, even though the platform owns the `PaymentIntent`. Think of the platform as facilitating a transaction between the connected account and the account’s customer. When you call the tax calculation API, calculate tax for the connected account by including the `Stripe-Account` header with the connected account ID.

Calculating tax for the connected account also simplifies tax reporting and filing by keeping the tax on connected account transactions separate from tax on transactions made directly with the platform.

#### Separate charges and transfers

For the Tax Calculation API calls:

- Include the `Stripe-Account` header with the value of the connected account ID.

```curl
curl https://api.stripe.com/v1/tax/calculations \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d currency=usd \
  -d "line_items[0][amount]"=1000 \
  -d "line_items[0][reference]"=L1 \
  -d customer="{{CUSTOMER_ID}}"
```

```cli
stripe tax calculations create  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  --currency=usd \
  -d "line_items[0][amount]"=1000 \
  -d "line_items[0][reference]"=L1 \
  --customer="{{CUSTOMER_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

calculation = client.v1.tax.calculations.create(
  {
    currency: 'usd',
    line_items: [
      {
        amount: 1000,
        reference: 'L1',
      },
    ],
    customer: '{{CUSTOMER_ID}}',
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
calculation = client.v1.tax.calculations.create(
  {
    "currency": "usd",
    "line_items": [{"amount": 1000, "reference": "L1"}],
    "customer": "{{CUSTOMER_ID}}",
  },
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$calculation = $stripe->tax->calculations->create(
  [
    'currency' => 'usd',
    'line_items' => [
      [
        'amount' => 1000,
        'reference' => 'L1',
      ],
    ],
    'customer' => '{{CUSTOMER_ID}}',
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
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

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

Calculation calculation = client.v1().tax().calculations().create(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const calculation = await stripe.tax.calculations.create(
  {
    currency: 'usd',
    line_items: [
      {
        amount: 1000,
        reference: 'L1',
      },
    ],
    customer: '{{CUSTOMER_ID}}',
  },
  {
    stripeAccount: '{{CONNECTEDACCOUNT_ID}}',
  }
);
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
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
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
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Tax.Calculations;
Stripe.Tax.Calculation calculation = service.Create(options, requestOptions);
```

For the Payment Intents API calls:

- Include [amount](https://docs.stripe.com/api/payment_intents/create.md#create_payment_intent-amount) with the `amount_total` returned by the tax calculation.
- Include [metadata[tax_calculation]](https://docs.stripe.com/api/payment_intents/create.md#create_payment_intent-metadata) with the `id` returned by the tax calculation.
- *Remember to include [on_behalf_of](https://docs.stripe.com/api/payment_intents/create.md#create_payment_intent-on_behalf_of) with the value of the connected account ID if the connected account is the [settlement merchant](https://docs.stripe.com/connect/separate-charges-and-transfers.md#settlement-merchant)*.

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

- Include [source_transaction](https://docs.stripe.com/api/transfers/create.md#create_transfer-source_transaction) to tie the transfer to the Payment Intent.
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

In this scenario, the connected account is liable for tax on the transaction, even though the platform owns the `PaymentIntent`. Think of the platform as facilitating a transaction between the connected account and the account’s customer. When you call the tax calculation API, calculate tax for the connected account by including the `Stripe-Account` header with the connected account ID.

Calculating tax for the connected account also simplifies tax reporting and filing by keeping the tax on connected account transactions separate from tax on transactions made directly with the platform.

You must also [create tax transactions](https://docs.stripe.com/tax/custom.md#tax-transaction) to record the tax you collect from customers and [account for refunds](https://docs.stripe.com/tax/custom.md#reversals).

### Off-Stripe payments

Check how to integrate using [Stripe Tax API](https://docs.stripe.com/tax/custom.md) and to allow the connected account to be *liable for tax* (The responsibility for collecting and reporting taxes for transactions in a Connect integration. It can belong to the platform or to connected accounts, depending on your business model, government regulations, and individual transaction details), include the `Stripe-Account` header with the value of the connected account ID in the Tax Calculation API calls:

```curl
curl https://api.stripe.com/v1/tax/calculations \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d currency=usd \
  -d "line_items[0][amount]"=1000 \
  -d "line_items[0][reference]"=L1 \
  -d customer="{{CUSTOMER_ID}}"
```

```cli
stripe tax calculations create  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  --currency=usd \
  -d "line_items[0][amount]"=1000 \
  -d "line_items[0][reference]"=L1 \
  --customer="{{CUSTOMER_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

calculation = client.v1.tax.calculations.create(
  {
    currency: 'usd',
    line_items: [
      {
        amount: 1000,
        reference: 'L1',
      },
    ],
    customer: '{{CUSTOMER_ID}}',
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
calculation = client.v1.tax.calculations.create(
  {
    "currency": "usd",
    "line_items": [{"amount": 1000, "reference": "L1"}],
    "customer": "{{CUSTOMER_ID}}",
  },
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$calculation = $stripe->tax->calculations->create(
  [
    'currency' => 'usd',
    'line_items' => [
      [
        'amount' => 1000,
        'reference' => 'L1',
      ],
    ],
    'customer' => '{{CUSTOMER_ID}}',
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
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

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

Calculation calculation = client.v1().tax().calculations().create(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const calculation = await stripe.tax.calculations.create(
  {
    currency: 'usd',
    line_items: [
      {
        amount: 1000,
        reference: 'L1',
      },
    ],
    customer: '{{CUSTOMER_ID}}',
  },
  {
    stripeAccount: '{{CONNECTEDACCOUNT_ID}}',
  }
);
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
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
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
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Tax.Calculations;
Stripe.Tax.Calculation calculation = service.Create(options, requestOptions);
```

You must also [create tax transactions](https://docs.stripe.com/tax/custom.md#tax-transaction) to record the tax you collect from customers and [account for refunds](https://docs.stripe.com/tax/custom.md#reversals).

After you implement it, Stripe automatically starts collecting tax in jurisdictions where the user has an active registration.

> Independent of the integration, your connected account receives a credit for the collected tax amount by default.

## Access Stripe Tax Reports

Your connected accounts can use [Stripe Tax reports](https://docs.stripe.com/tax/reports.md) to help them correctly file and remit tax.

### Connected account use the Stripe Dashboard

This option is only available to accounts with access to the Stripe Dashboard (for example, Standard accounts).

The connected accounts can access their Stripe Tax reports using the [Tax Reporting](https://docs.stripe.com/tax/reports.md#how-to-access-data-using-exports-and-reports) functionality in the Stripe Dashboard.

### Use the Stripe API

Use this option for accounts without access to the Stripe Dashboard (for example, Custom and Express accounts).

Platforms can download [itemized tax transactions](https://docs.stripe.com/tax/reports.md#itemized-exports) for their connected accounts using the [Report API](https://docs.stripe.com/reports/api.md) with the [connected_account_tax.transactions.itemized.2](https://docs.stripe.com/reports/report-types/connect.md) report type.

When a platform runs the following command, they download all 2022 transactions from all connected accounts:

```curl
curl https://api.stripe.com/v1/reporting/report_runs \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d report_type="connected_account_tax.transactions.itemized.2" \
  -d "parameters[interval_start]"=1641013200 \
  -d "parameters[interval_end]"=1672549200
```

```cli
stripe reporting report_runs create  \
  --report-type="connected_account_tax.transactions.itemized.2" \
  -d "parameters[interval_start]"=1641013200 \
  -d "parameters[interval_end]"=1672549200
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

report_run = client.v1.reporting.report_runs.create({
  report_type: 'connected_account_tax.transactions.itemized.2',
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
  "report_type": "connected_account_tax.transactions.itemized.2",
  "parameters": {"interval_start": 1641013200, "interval_end": 1672549200},
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$reportRun = $stripe->reporting->reportRuns->create([
  'report_type' => 'connected_account_tax.transactions.itemized.2',
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
    .setReportType("connected_account_tax.transactions.itemized.2")
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
  report_type: 'connected_account_tax.transactions.itemized.2',
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
  ReportType: stripe.String("connected_account_tax.transactions.itemized.2"),
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
    ReportType = "connected_account_tax.transactions.itemized.2",
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

When a platform runs the following command, they download all 2022 transactions from a single connected account:

> To generate reports for connected accounts, use the `connected_account` parameter instead of the `Stripe-Account` header.

```curl
curl https://api.stripe.com/v1/reporting/report_runs \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d report_type="connected_account_tax.transactions.itemized.2" \
  -d "parameters[interval_start]"=1641013200 \
  -d "parameters[interval_end]"=1672549200 \
  -d "parameters[connected_account]"={{CONNECTED_ACCOUNT_ID}}
```

```cli
stripe reporting report_runs create  \
  --report-type="connected_account_tax.transactions.itemized.2" \
  -d "parameters[interval_start]"=1641013200 \
  -d "parameters[interval_end]"=1672549200 \
  -d "parameters[connected_account]"={{CONNECTED_ACCOUNT_ID}}
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

report_run = client.v1.reporting.report_runs.create({
  report_type: 'connected_account_tax.transactions.itemized.2',
  parameters: {
    interval_start: 1641013200,
    interval_end: 1672549200,
    connected_account: '{{CONNECTED_ACCOUNT_ID}}',
  },
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
report_run = client.v1.reporting.report_runs.create({
  "report_type": "connected_account_tax.transactions.itemized.2",
  "parameters": {
    "interval_start": 1641013200,
    "interval_end": 1672549200,
    "connected_account": "{{CONNECTED_ACCOUNT_ID}}",
  },
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$reportRun = $stripe->reporting->reportRuns->create([
  'report_type' => 'connected_account_tax.transactions.itemized.2',
  'parameters' => [
    'interval_start' => 1641013200,
    'interval_end' => 1672549200,
    'connected_account' => '{{CONNECTED_ACCOUNT_ID}}',
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

ReportRunCreateParams params =
  ReportRunCreateParams.builder()
    .setReportType("connected_account_tax.transactions.itemized.2")
    .setParameters(
      ReportRunCreateParams.Parameters.builder()
        .setIntervalStart(1641013200L)
        .setIntervalEnd(1672549200L)
        .setConnectedAccount("{{CONNECTED_ACCOUNT_ID}}")
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
  report_type: 'connected_account_tax.transactions.itemized.2',
  parameters: {
    interval_start: 1641013200,
    interval_end: 1672549200,
    connected_account: '{{CONNECTED_ACCOUNT_ID}}',
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.ReportingReportRunCreateParams{
  ReportType: stripe.String("connected_account_tax.transactions.itemized.2"),
  Parameters: &stripe.ReportingReportRunCreateParametersParams{
    IntervalStart: stripe.Int64(1641013200),
    IntervalEnd: stripe.Int64(1672549200),
    ConnectedAccount: stripe.String("{{CONNECTED_ACCOUNT_ID}}"),
  },
}
result, err := sc.V1ReportingReportRuns.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Reporting.ReportRunCreateOptions
{
    ReportType = "connected_account_tax.transactions.itemized.2",
    Parameters = new Stripe.Reporting.ReportRunParametersOptions
    {
        IntervalStart = DateTimeOffset.FromUnixTimeSeconds(1641013200).UtcDateTime,
        IntervalEnd = DateTimeOffset.FromUnixTimeSeconds(1672549200).UtcDateTime,
        ConnectedAccount = "{{CONNECTED_ACCOUNT_ID}}",
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Reporting.ReportRuns;
Stripe.Reporting.ReportRun reportRun = service.Create(options);
```

### Use the Export Tax Transactions Embedded Component

To learn more about this component and integrate it, see [export tax transactions](https://docs.stripe.com/connect/supported-embedded-components/export-tax-transactions.md).

## See also

- [Calculate tax in your custom checkout flow](https://docs.stripe.com/tax/custom.md)
