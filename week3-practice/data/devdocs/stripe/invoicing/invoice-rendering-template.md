# Invoice rendering templates

Use invoice rendering templates to personalize your invoice appearance for different customers

You can tailor content on your invoices for specific groups of customers. Examples might include:

- Displaying specific text in your invoice’s footer field for customers from a particular country
- Displaying a specific memo note for customers with a particular revenue channel
- Grouping line items in a certain manner for customers with complex transactions

This guide describes how you can use invoice rendering templates to:

- Store and reuse common values for invoice fields instead of entering them for every relevant invoice
- Store, manage, and update invoice field values that apply to large sets of customers
- Configure Line Item Grouping rules, which can’t be set outside of invoice rendering templates
- For subscription invoices, customize invoice values without interacting directly with the invoice

## Set up invoice rendering templates

You can create invoice rendering templates only in the Dashboard. You can’t create them using the API.

1. In your Billing settings, select the [Invoices > Templates tab](https://dashboard.stripe.com/settings/billing/invoice-templates).
1. Click **+ Create template**.
1. Enter a name for your template.
1. Specify values for the fields you want to include in the template. Invoice templates support the memo, footer, and custom fields.
1. Optionally, you can [define one or more line item groups using Common Expression Language (CEL) expressions](https://docs.stripe.com/invoicing/group-line-items.md).
1. To see a preview of your template, enter the ID of an applicable invoice in the **Invoice ID** field of the Preview pane. However, any values set directly on that invoice override the corresponding template value. For example, if that invoice has a footer value, the preview displays the invoice’s footer, not the template’s footer.

## Apply invoice rendering templates to invoices

#### Dashboard

In the Dashboard, you can apply a template to invoices in the following ways:

- In the Invoice editor
- In the Subscriptions editor, to apply to all invoices associated with the subscription
- In a customer’s invoice settings, to apply to all future invoices associated with that customer

#### API

With the API, you can apply a template to invoices in the following ways:

- When you create a draft invoice using the [Invoicing API](https://docs.stripe.com/api/invoices/create.md#create_invoice-rendering-template)

```curl
curl https://api.stripe.com/v1/invoices \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer=cus_xxx \
  -d collection_method=send_invoice \
  -d days_until_due=30 \
  -d "rendering[template]"=inrtem_xxx
```

```cli
stripe invoices create  \
  --customer=cus_xxx \
  --collection-method=send_invoice \
  --days-until-due=30 \
  -d "rendering[template]"=inrtem_xxx
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

invoice = client.v1.invoices.create({
  customer: 'cus_xxx',
  collection_method: 'send_invoice',
  days_until_due: 30,
  rendering: {template: 'inrtem_xxx'},
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
invoice = client.v1.invoices.create({
  "customer": "cus_xxx",
  "collection_method": "send_invoice",
  "days_until_due": 30,
  "rendering": {"template": "inrtem_xxx"},
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$invoice = $stripe->invoices->create([
  'customer' => 'cus_xxx',
  'collection_method' => 'send_invoice',
  'days_until_due' => 30,
  'rendering' => ['template' => 'inrtem_xxx'],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

InvoiceCreateParams params =
  InvoiceCreateParams.builder()
    .setCustomer("cus_xxx")
    .setCollectionMethod(InvoiceCreateParams.CollectionMethod.SEND_INVOICE)
    .setDaysUntilDue(30L)
    .setRendering(
      InvoiceCreateParams.Rendering.builder().setTemplate("inrtem_xxx").build()
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
  customer: 'cus_xxx',
  collection_method: 'send_invoice',
  days_until_due: 30,
  rendering: {
    template: 'inrtem_xxx',
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.InvoiceCreateParams{
  Customer: stripe.String("cus_xxx"),
  CollectionMethod: stripe.String(stripe.InvoiceCollectionMethodSendInvoice),
  DaysUntilDue: stripe.Int64(30),
  Rendering: &stripe.InvoiceCreateRenderingParams{Template: stripe.String("inrtem_xxx")},
}
result, err := sc.V1Invoices.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new InvoiceCreateOptions
{
    Customer = "cus_xxx",
    CollectionMethod = "send_invoice",
    DaysUntilDue = 30,
    Rendering = new InvoiceRenderingOptions { Template = "inrtem_xxx" },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Invoices;
Invoice invoice = service.Create(options);
```

- When you create or update a customer using the [Customer API](https://docs.stripe.com/api/customers/create.md#create_customer-invoice_settings-rendering_options-template), to apply to all future invoices associated with that customer, including subscription-generated invoices

```curl
curl https://api.stripe.com/v1/customers \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d name="John Doe" \
  -d "invoice_settings[rendering_options][template]"=inrtem_xxx
```

```cli
stripe customers create  \
  --name="John Doe" \
  -d "invoice_settings[rendering_options][template]"=inrtem_xxx
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

customer = client.v1.customers.create({
  name: 'John Doe',
  invoice_settings: {rendering_options: {template: 'inrtem_xxx'}},
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
customer = client.v1.customers.create({
  "name": "John Doe",
  "invoice_settings": {"rendering_options": {"template": "inrtem_xxx"}},
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$customer = $stripe->customers->create([
  'name' => 'John Doe',
  'invoice_settings' => ['rendering_options' => ['template' => 'inrtem_xxx']],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CustomerCreateParams params =
  CustomerCreateParams.builder()
    .setName("John Doe")
    .setInvoiceSettings(
      CustomerCreateParams.InvoiceSettings.builder()
        .setRenderingOptions(
          CustomerCreateParams.InvoiceSettings.RenderingOptions.builder()
            .setTemplate("inrtem_xxx")
            .build()
        )
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Customer customer = client.v1().customers().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const customer = await stripe.customers.create({
  name: 'John Doe',
  invoice_settings: {
    rendering_options: {
      template: 'inrtem_xxx',
    },
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CustomerCreateParams{
  Name: stripe.String("John Doe"),
  InvoiceSettings: &stripe.CustomerCreateInvoiceSettingsParams{
    RenderingOptions: &stripe.CustomerCreateInvoiceSettingsRenderingOptionsParams{
      Template: stripe.String("inrtem_xxx"),
    },
  },
}
result, err := sc.V1Customers.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new CustomerCreateOptions
{
    Name = "John Doe",
    InvoiceSettings = new CustomerInvoiceSettingsOptions
    {
        RenderingOptions = new CustomerInvoiceSettingsRenderingOptionsOptions
        {
            Template = "inrtem_xxx",
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Customers;
Customer customer = service.Create(options);
```

> You can’t use the API to apply invoice templates directly to subscriptions, subscription schedules, or quotes. Instead, you can attach a template to a customer to apply to all future invoices associated with that customer, or apply a template to invoices while they’re in draft status.

## Override and update templates

You can configure invoice footer, memo, and custom field values in multiple places. When multiple values can apply to an invoice field, they’re prioritized in the following order, from highest to lowest:

| Priority                             | Setting                           | Method           |
| ------------------------------------ | --------------------------------- | ---------------- |
| 1                                    | On the invoice                    | Dashboard or API |
| On the subscription                  | Dashboard only                    |
| 2                                    | Template applied to the invoice   | Dashboard or API |
| Template applied to the subscription | Dashboard only                    |
| 3                                    | Template attached to the customer | Dashboard or API |
| 4                                    | Invoice settings on the customer  | API only         |
| 5                                    | Invoice settings on the account   | Dashboard only   |

> Defined invoice settings always apply unless overridden by a higher-priority setting. For example, to create a one-time invoice for a customer that overrides the customer’s attached template, you must either apply a different template to the invoice or set the invoice values directly.

When you update or replace a template, the new values apply to all future invoices associated with the template. For example, if you update a value in a customer’s template, the new value applies to all future invoices for that customer’s existing subscriptions unless the subscription template overrides it.
