# Add custom fields

Add additional fields to a prebuilt payment page with Checkout.

You can add custom fields on the payment form to collect additional information from your customers. The information is available after the payment is complete and is useful for fulfilling the purchase.

Custom fields have the following limitations:

- Up to three fields allowed.
- Not available in `setup` mode.
- Support for up to 255 characters on text fields.
- Support for up to 255 digits on numeric fields.
- Support for up to 200 options on dropdown fields.

> Don’t use custom fields to collect personal, protected, or sensitive data, or information restricted by law.

## Create a Checkout Session

Create a Checkout Session while specifying an array of [custom fields](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-custom_fields). Each field must have a unique `key` that your integration uses to reconcile the field. Also provide a label for the field that you display to your customer. Labels for custom fields aren’t translated, but you can use the [locale](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-locale) parameter to set the language of your Checkout Session to match the same language as your labels.

#### Stripe-hosted page

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d mode=payment \
  --data-urlencode success_url="https://example.com/success" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "custom_fields[0][key]"=engraving \
  -d "custom_fields[0][label][type]"=custom \
  -d "custom_fields[0][label][custom]"="Personalized engraving" \
  -d "custom_fields[0][type]"=text
```

```cli
stripe checkout sessions create  \
  --mode=payment \
  --success-url="https://example.com/success" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "custom_fields[0][key]"=engraving \
  -d "custom_fields[0][label][type]"=custom \
  -d "custom_fields[0][label][custom]"="Personalized engraving" \
  -d "custom_fields[0][type]"=text
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  mode: 'payment',
  success_url: 'https://example.com/success',
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  custom_fields: [
    {
      key: 'engraving',
      label: {
        type: 'custom',
        custom: 'Personalized engraving',
      },
      type: 'text',
    },
  ],
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "mode": "payment",
  "success_url": "https://example.com/success",
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 1}],
  "custom_fields": [
    {
      "key": "engraving",
      "label": {"type": "custom", "custom": "Personalized engraving"},
      "type": "text",
    },
  ],
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'mode' => 'payment',
  'success_url' => 'https://example.com/success',
  'line_items' => [
    [
      'price' => '{{PRICE_ID}}',
      'quantity' => 1,
    ],
  ],
  'custom_fields' => [
    [
      'key' => 'engraving',
      'label' => [
        'type' => 'custom',
        'custom' => 'Personalized engraving',
      ],
      'type' => 'text',
    ],
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setSuccessUrl("https://example.com/success")
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPrice("{{PRICE_ID}}")
        .setQuantity(1L)
        .build()
    )
    .addCustomField(
      SessionCreateParams.CustomField.builder()
        .setKey("engraving")
        .setLabel(
          SessionCreateParams.CustomField.Label.builder()
            .setType(SessionCreateParams.CustomField.Label.Type.CUSTOM)
            .setCustom("Personalized engraving")
            .build()
        )
        .setType(SessionCreateParams.CustomField.Type.TEXT)
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session = client.v1().checkout().sessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  mode: 'payment',
  success_url: 'https://example.com/success',
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  custom_fields: [
    {
      key: 'engraving',
      label: {
        type: 'custom',
        custom: 'Personalized engraving',
      },
      type: 'text',
    },
  ],
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  SuccessURL: stripe.String("https://example.com/success"),
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(1),
    },
  },
  CustomFields: []*stripe.CheckoutSessionCreateCustomFieldParams{
    &stripe.CheckoutSessionCreateCustomFieldParams{
      Key: stripe.String("engraving"),
      Label: &stripe.CheckoutSessionCreateCustomFieldLabelParams{
        Type: stripe.String("custom"),
        Custom: stripe.String("Personalized engraving"),
      },
      Type: stripe.String(stripe.CheckoutSessionCustomFieldTypeText),
    },
  },
}
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionCreateOptions
{
    Mode = "payment",
    SuccessUrl = "https://example.com/success",
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            Price = "{{PRICE_ID}}",
            Quantity = 1,
        },
    },
    CustomFields = new List<Stripe.Checkout.SessionCustomFieldOptions>
    {
        new Stripe.Checkout.SessionCustomFieldOptions
        {
            Key = "engraving",
            Label = new Stripe.Checkout.SessionCustomFieldLabelOptions
            {
                Type = "custom",
                Custom = "Personalized engraving",
            },
            Type = "text",
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

#### Embedded form

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d mode=payment \
  -d ui_mode=embedded \
  --data-urlencode return_url="https://example.com/return" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "custom_fields[0][key]"=engraving \
  -d "custom_fields[0][label][type]"=custom \
  -d "custom_fields[0][label][custom]"="Personalized engraving" \
  -d "custom_fields[0][type]"=text
```

```cli
stripe checkout sessions create  \
  --mode=payment \
  --ui-mode=embedded \
  --return-url="https://example.com/return" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "custom_fields[0][key]"=engraving \
  -d "custom_fields[0][label][type]"=custom \
  -d "custom_fields[0][label][custom]"="Personalized engraving" \
  -d "custom_fields[0][type]"=text
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  mode: 'payment',
  ui_mode: 'embedded',
  return_url: 'https://example.com/return',
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  custom_fields: [
    {
      key: 'engraving',
      label: {
        type: 'custom',
        custom: 'Personalized engraving',
      },
      type: 'text',
    },
  ],
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "mode": "payment",
  "ui_mode": "embedded",
  "return_url": "https://example.com/return",
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 1}],
  "custom_fields": [
    {
      "key": "engraving",
      "label": {"type": "custom", "custom": "Personalized engraving"},
      "type": "text",
    },
  ],
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'mode' => 'payment',
  'ui_mode' => 'embedded',
  'return_url' => 'https://example.com/return',
  'line_items' => [
    [
      'price' => '{{PRICE_ID}}',
      'quantity' => 1,
    ],
  ],
  'custom_fields' => [
    [
      'key' => 'engraving',
      'label' => [
        'type' => 'custom',
        'custom' => 'Personalized engraving',
      ],
      'type' => 'text',
    ],
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setUiMode(SessionCreateParams.UiMode.EMBEDDED)
    .setReturnUrl("https://example.com/return")
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPrice("{{PRICE_ID}}")
        .setQuantity(1L)
        .build()
    )
    .addCustomField(
      SessionCreateParams.CustomField.builder()
        .setKey("engraving")
        .setLabel(
          SessionCreateParams.CustomField.Label.builder()
            .setType(SessionCreateParams.CustomField.Label.Type.CUSTOM)
            .setCustom("Personalized engraving")
            .build()
        )
        .setType(SessionCreateParams.CustomField.Type.TEXT)
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session = client.v1().checkout().sessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  mode: 'payment',
  ui_mode: 'embedded',
  return_url: 'https://example.com/return',
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  custom_fields: [
    {
      key: 'engraving',
      label: {
        type: 'custom',
        custom: 'Personalized engraving',
      },
      type: 'text',
    },
  ],
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  UIMode: stripe.String(stripe.CheckoutSessionUIModeEmbedded),
  ReturnURL: stripe.String("https://example.com/return"),
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(1),
    },
  },
  CustomFields: []*stripe.CheckoutSessionCreateCustomFieldParams{
    &stripe.CheckoutSessionCreateCustomFieldParams{
      Key: stripe.String("engraving"),
      Label: &stripe.CheckoutSessionCreateCustomFieldLabelParams{
        Type: stripe.String("custom"),
        Custom: stripe.String("Personalized engraving"),
      },
      Type: stripe.String(stripe.CheckoutSessionCustomFieldTypeText),
    },
  },
}
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionCreateOptions
{
    Mode = "payment",
    UiMode = "embedded",
    ReturnUrl = "https://example.com/return",
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            Price = "{{PRICE_ID}}",
            Quantity = 1,
        },
    },
    CustomFields = new List<Stripe.Checkout.SessionCustomFieldOptions>
    {
        new Stripe.Checkout.SessionCustomFieldOptions
        {
            Key = "engraving",
            Label = new Stripe.Checkout.SessionCustomFieldLabelOptions
            {
                Type = "custom",
                Custom = "Personalized engraving",
            },
            Type = "text",
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```
![](https://d37ugbyn3rpeym.cloudfront.net/videos/checkout/custom_fields_embedded.mov)
## Retrieve custom fields

When your customer completes the Checkout Session, we send a [checkout.session.completed](https://docs.stripe.com/api/events/types.md#event_types-checkout.session.completed) *webhook* (A webhook is a real-time push notification sent to your application as a JSON payload through HTTPS requests) with the completed fields.

Example `checkout.session.completed` payload:

```json
{
  "id": "evt_1Ep24XHssDVaQm2PpwS19Yt0",
  "object": "event",
  "api_version": "2022-11-15",
  "created": 1664928000,
  "data": {
    "object": {
      "id": "cs_test_MlZAaTXUMHjWZ7DcXjusJnDU4MxPalbtL5eYrmS2GKxqscDtpJq8QM0k",
      "object": "checkout.session","custom_fields": [{
        "key": "engraving",
        "label": {
          "type": "custom",
          "custom": "Personalized engraving"
        },
        "optional": false,
        "type": "text",
        "text": {
          "value": "Jane"
        }
      }],
      "mode": "payment"
    }
  },
  "livemode": false,
  "pending_webhooks": 1,
  "request": {
    "id": null,
    "idempotency_key": null
  },
  "type": "checkout.session.completed"
}
```

You can also look up and edit custom field values from the Dashboard, by clicking into a specific payment in the [Transactions](https://dashboard.stripe.com/payments) tab or including custom field values when exporting your payments from the [Dashboard](https://dashboard.stripe.com/payments).

## Use a custom field

### Mark a field as optional 

By default, customers must complete all fields before completing payment. To mark a field as optional, pass in `optional=true`.

#### Stripe-hosted page

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d mode=payment \
  --data-urlencode success_url="https://example.com/success" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "custom_fields[0][key]"=engraving \
  -d "custom_fields[0][label][type]"=custom \
  -d "custom_fields[0][label][custom]"="Personalized engraving" \
  -d "custom_fields[0][type]"=text \
  -d "custom_fields[0][optional]"=true
```

```cli
stripe checkout sessions create  \
  --mode=payment \
  --success-url="https://example.com/success" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "custom_fields[0][key]"=engraving \
  -d "custom_fields[0][label][type]"=custom \
  -d "custom_fields[0][label][custom]"="Personalized engraving" \
  -d "custom_fields[0][type]"=text \
  -d "custom_fields[0][optional]"=true
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  mode: 'payment',
  success_url: 'https://example.com/success',
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  custom_fields: [
    {
      key: 'engraving',
      label: {
        type: 'custom',
        custom: 'Personalized engraving',
      },
      type: 'text',
      optional: true,
    },
  ],
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "mode": "payment",
  "success_url": "https://example.com/success",
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 1}],
  "custom_fields": [
    {
      "key": "engraving",
      "label": {"type": "custom", "custom": "Personalized engraving"},
      "type": "text",
      "optional": True,
    },
  ],
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'mode' => 'payment',
  'success_url' => 'https://example.com/success',
  'line_items' => [
    [
      'price' => '{{PRICE_ID}}',
      'quantity' => 1,
    ],
  ],
  'custom_fields' => [
    [
      'key' => 'engraving',
      'label' => [
        'type' => 'custom',
        'custom' => 'Personalized engraving',
      ],
      'type' => 'text',
      'optional' => true,
    ],
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setSuccessUrl("https://example.com/success")
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPrice("{{PRICE_ID}}")
        .setQuantity(1L)
        .build()
    )
    .addCustomField(
      SessionCreateParams.CustomField.builder()
        .setKey("engraving")
        .setLabel(
          SessionCreateParams.CustomField.Label.builder()
            .setType(SessionCreateParams.CustomField.Label.Type.CUSTOM)
            .setCustom("Personalized engraving")
            .build()
        )
        .setType(SessionCreateParams.CustomField.Type.TEXT)
        .setOptional(true)
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session = client.v1().checkout().sessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  mode: 'payment',
  success_url: 'https://example.com/success',
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  custom_fields: [
    {
      key: 'engraving',
      label: {
        type: 'custom',
        custom: 'Personalized engraving',
      },
      type: 'text',
      optional: true,
    },
  ],
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  SuccessURL: stripe.String("https://example.com/success"),
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(1),
    },
  },
  CustomFields: []*stripe.CheckoutSessionCreateCustomFieldParams{
    &stripe.CheckoutSessionCreateCustomFieldParams{
      Key: stripe.String("engraving"),
      Label: &stripe.CheckoutSessionCreateCustomFieldLabelParams{
        Type: stripe.String("custom"),
        Custom: stripe.String("Personalized engraving"),
      },
      Type: stripe.String(stripe.CheckoutSessionCustomFieldTypeText),
      Optional: stripe.Bool(true),
    },
  },
}
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionCreateOptions
{
    Mode = "payment",
    SuccessUrl = "https://example.com/success",
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            Price = "{{PRICE_ID}}",
            Quantity = 1,
        },
    },
    CustomFields = new List<Stripe.Checkout.SessionCustomFieldOptions>
    {
        new Stripe.Checkout.SessionCustomFieldOptions
        {
            Key = "engraving",
            Label = new Stripe.Checkout.SessionCustomFieldLabelOptions
            {
                Type = "custom",
                Custom = "Personalized engraving",
            },
            Type = "text",
            Optional = true,
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

#### Embedded form

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d mode=payment \
  -d ui_mode=embedded \
  --data-urlencode return_url="https://example.com/return" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "custom_fields[0][key]"=engraving \
  -d "custom_fields[0][label][type]"=custom \
  -d "custom_fields[0][label][custom]"="Personalized engraving" \
  -d "custom_fields[0][type]"=text \
  -d "custom_fields[0][optional]"=true
```

```cli
stripe checkout sessions create  \
  --mode=payment \
  --ui-mode=embedded \
  --return-url="https://example.com/return" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "custom_fields[0][key]"=engraving \
  -d "custom_fields[0][label][type]"=custom \
  -d "custom_fields[0][label][custom]"="Personalized engraving" \
  -d "custom_fields[0][type]"=text \
  -d "custom_fields[0][optional]"=true
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  mode: 'payment',
  ui_mode: 'embedded',
  return_url: 'https://example.com/return',
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  custom_fields: [
    {
      key: 'engraving',
      label: {
        type: 'custom',
        custom: 'Personalized engraving',
      },
      type: 'text',
      optional: true,
    },
  ],
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "mode": "payment",
  "ui_mode": "embedded",
  "return_url": "https://example.com/return",
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 1}],
  "custom_fields": [
    {
      "key": "engraving",
      "label": {"type": "custom", "custom": "Personalized engraving"},
      "type": "text",
      "optional": True,
    },
  ],
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'mode' => 'payment',
  'ui_mode' => 'embedded',
  'return_url' => 'https://example.com/return',
  'line_items' => [
    [
      'price' => '{{PRICE_ID}}',
      'quantity' => 1,
    ],
  ],
  'custom_fields' => [
    [
      'key' => 'engraving',
      'label' => [
        'type' => 'custom',
        'custom' => 'Personalized engraving',
      ],
      'type' => 'text',
      'optional' => true,
    ],
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setUiMode(SessionCreateParams.UiMode.EMBEDDED)
    .setReturnUrl("https://example.com/return")
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPrice("{{PRICE_ID}}")
        .setQuantity(1L)
        .build()
    )
    .addCustomField(
      SessionCreateParams.CustomField.builder()
        .setKey("engraving")
        .setLabel(
          SessionCreateParams.CustomField.Label.builder()
            .setType(SessionCreateParams.CustomField.Label.Type.CUSTOM)
            .setCustom("Personalized engraving")
            .build()
        )
        .setType(SessionCreateParams.CustomField.Type.TEXT)
        .setOptional(true)
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session = client.v1().checkout().sessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  mode: 'payment',
  ui_mode: 'embedded',
  return_url: 'https://example.com/return',
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  custom_fields: [
    {
      key: 'engraving',
      label: {
        type: 'custom',
        custom: 'Personalized engraving',
      },
      type: 'text',
      optional: true,
    },
  ],
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  UIMode: stripe.String(stripe.CheckoutSessionUIModeEmbedded),
  ReturnURL: stripe.String("https://example.com/return"),
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(1),
    },
  },
  CustomFields: []*stripe.CheckoutSessionCreateCustomFieldParams{
    &stripe.CheckoutSessionCreateCustomFieldParams{
      Key: stripe.String("engraving"),
      Label: &stripe.CheckoutSessionCreateCustomFieldLabelParams{
        Type: stripe.String("custom"),
        Custom: stripe.String("Personalized engraving"),
      },
      Type: stripe.String(stripe.CheckoutSessionCustomFieldTypeText),
      Optional: stripe.Bool(true),
    },
  },
}
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionCreateOptions
{
    Mode = "payment",
    UiMode = "embedded",
    ReturnUrl = "https://example.com/return",
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            Price = "{{PRICE_ID}}",
            Quantity = 1,
        },
    },
    CustomFields = new List<Stripe.Checkout.SessionCustomFieldOptions>
    {
        new Stripe.Checkout.SessionCustomFieldOptions
        {
            Key = "engraving",
            Label = new Stripe.Checkout.SessionCustomFieldLabelOptions
            {
                Type = "custom",
                Custom = "Personalized engraving",
            },
            Type = "text",
            Optional = true,
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```
![](https://b.stripecdn.com/docs-statics-srv/assets/optional.bf0c1564296ff02264bd5e8c066a6034.png)

### Add a dropdown field 

A dropdown field presents your customers with a list of options to select from. To create a dropdown field, specify `type=dropdown` and a list of options, each with a `label` and a `value`. The `label` displays to the customer while your integration uses the `value` to reconcile which option the customer selected.

#### Stripe-hosted page

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d mode=payment \
  --data-urlencode success_url="https://example.com/success" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "custom_fields[0][key]"=sample \
  -d "custom_fields[0][label][type]"=custom \
  -d "custom_fields[0][label][custom]"="Free sample" \
  -d "custom_fields[0][optional]"=true \
  -d "custom_fields[0][type]"=dropdown \
  -d "custom_fields[0][dropdown][options][0][label]"="Balm sample" \
  -d "custom_fields[0][dropdown][options][0][value]"=balm \
  -d "custom_fields[0][dropdown][options][1][label]"="BB cream sample" \
  -d "custom_fields[0][dropdown][options][1][value]"=cream
```

```cli
stripe checkout sessions create  \
  --mode=payment \
  --success-url="https://example.com/success" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "custom_fields[0][key]"=sample \
  -d "custom_fields[0][label][type]"=custom \
  -d "custom_fields[0][label][custom]"="Free sample" \
  -d "custom_fields[0][optional]"=true \
  -d "custom_fields[0][type]"=dropdown \
  -d "custom_fields[0][dropdown][options][0][label]"="Balm sample" \
  -d "custom_fields[0][dropdown][options][0][value]"=balm \
  -d "custom_fields[0][dropdown][options][1][label]"="BB cream sample" \
  -d "custom_fields[0][dropdown][options][1][value]"=cream
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  mode: 'payment',
  success_url: 'https://example.com/success',
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  custom_fields: [
    {
      key: 'sample',
      label: {
        type: 'custom',
        custom: 'Free sample',
      },
      optional: true,
      type: 'dropdown',
      dropdown: {
        options: [
          {
            label: 'Balm sample',
            value: 'balm',
          },
          {
            label: 'BB cream sample',
            value: 'cream',
          },
        ],
      },
    },
  ],
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "mode": "payment",
  "success_url": "https://example.com/success",
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 1}],
  "custom_fields": [
    {
      "key": "sample",
      "label": {"type": "custom", "custom": "Free sample"},
      "optional": True,
      "type": "dropdown",
      "dropdown": {
        "options": [
          {"label": "Balm sample", "value": "balm"},
          {"label": "BB cream sample", "value": "cream"},
        ],
      },
    },
  ],
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'mode' => 'payment',
  'success_url' => 'https://example.com/success',
  'line_items' => [
    [
      'price' => '{{PRICE_ID}}',
      'quantity' => 1,
    ],
  ],
  'custom_fields' => [
    [
      'key' => 'sample',
      'label' => [
        'type' => 'custom',
        'custom' => 'Free sample',
      ],
      'optional' => true,
      'type' => 'dropdown',
      'dropdown' => [
        'options' => [
          [
            'label' => 'Balm sample',
            'value' => 'balm',
          ],
          [
            'label' => 'BB cream sample',
            'value' => 'cream',
          ],
        ],
      ],
    ],
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setSuccessUrl("https://example.com/success")
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPrice("{{PRICE_ID}}")
        .setQuantity(1L)
        .build()
    )
    .addCustomField(
      SessionCreateParams.CustomField.builder()
        .setKey("sample")
        .setLabel(
          SessionCreateParams.CustomField.Label.builder()
            .setType(SessionCreateParams.CustomField.Label.Type.CUSTOM)
            .setCustom("Free sample")
            .build()
        )
        .setOptional(true)
        .setType(SessionCreateParams.CustomField.Type.DROPDOWN)
        .setDropdown(
          SessionCreateParams.CustomField.Dropdown.builder()
            .addOption(
              SessionCreateParams.CustomField.Dropdown.Option.builder()
                .setLabel("Balm sample")
                .setValue("balm")
                .build()
            )
            .addOption(
              SessionCreateParams.CustomField.Dropdown.Option.builder()
                .setLabel("BB cream sample")
                .setValue("cream")
                .build()
            )
            .build()
        )
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session = client.v1().checkout().sessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  mode: 'payment',
  success_url: 'https://example.com/success',
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  custom_fields: [
    {
      key: 'sample',
      label: {
        type: 'custom',
        custom: 'Free sample',
      },
      optional: true,
      type: 'dropdown',
      dropdown: {
        options: [
          {
            label: 'Balm sample',
            value: 'balm',
          },
          {
            label: 'BB cream sample',
            value: 'cream',
          },
        ],
      },
    },
  ],
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  SuccessURL: stripe.String("https://example.com/success"),
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(1),
    },
  },
  CustomFields: []*stripe.CheckoutSessionCreateCustomFieldParams{
    &stripe.CheckoutSessionCreateCustomFieldParams{
      Key: stripe.String("sample"),
      Label: &stripe.CheckoutSessionCreateCustomFieldLabelParams{
        Type: stripe.String("custom"),
        Custom: stripe.String("Free sample"),
      },
      Optional: stripe.Bool(true),
      Type: stripe.String(stripe.CheckoutSessionCustomFieldTypeDropdown),
      Dropdown: &stripe.CheckoutSessionCreateCustomFieldDropdownParams{
        Options: []*stripe.CheckoutSessionCreateCustomFieldDropdownOptionParams{
          &stripe.CheckoutSessionCreateCustomFieldDropdownOptionParams{
            Label: stripe.String("Balm sample"),
            Value: stripe.String("balm"),
          },
          &stripe.CheckoutSessionCreateCustomFieldDropdownOptionParams{
            Label: stripe.String("BB cream sample"),
            Value: stripe.String("cream"),
          },
        },
      },
    },
  },
}
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionCreateOptions
{
    Mode = "payment",
    SuccessUrl = "https://example.com/success",
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            Price = "{{PRICE_ID}}",
            Quantity = 1,
        },
    },
    CustomFields = new List<Stripe.Checkout.SessionCustomFieldOptions>
    {
        new Stripe.Checkout.SessionCustomFieldOptions
        {
            Key = "sample",
            Label = new Stripe.Checkout.SessionCustomFieldLabelOptions
            {
                Type = "custom",
                Custom = "Free sample",
            },
            Optional = true,
            Type = "dropdown",
            Dropdown = new Stripe.Checkout.SessionCustomFieldDropdownOptions
            {
                Options = new List<Stripe.Checkout.SessionCustomFieldDropdownOptionOptions>
                {
                    new Stripe.Checkout.SessionCustomFieldDropdownOptionOptions
                    {
                        Label = "Balm sample",
                        Value = "balm",
                    },
                    new Stripe.Checkout.SessionCustomFieldDropdownOptionOptions
                    {
                        Label = "BB cream sample",
                        Value = "cream",
                    },
                },
            },
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

#### Embedded form

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d mode=payment \
  -d ui_mode=embedded \
  --data-urlencode return_url="https://example.com/return" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "custom_fields[0][key]"=sample \
  -d "custom_fields[0][label][type]"=custom \
  -d "custom_fields[0][label][custom]"="Free sample" \
  -d "custom_fields[0][optional]"=true \
  -d "custom_fields[0][type]"=dropdown \
  -d "custom_fields[0][dropdown][options][0][label]"="Balm sample" \
  -d "custom_fields[0][dropdown][options][0][value]"=balm \
  -d "custom_fields[0][dropdown][options][1][label]"="BB cream sample" \
  -d "custom_fields[0][dropdown][options][1][value]"=cream
```

```cli
stripe checkout sessions create  \
  --mode=payment \
  --ui-mode=embedded \
  --return-url="https://example.com/return" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "custom_fields[0][key]"=sample \
  -d "custom_fields[0][label][type]"=custom \
  -d "custom_fields[0][label][custom]"="Free sample" \
  -d "custom_fields[0][optional]"=true \
  -d "custom_fields[0][type]"=dropdown \
  -d "custom_fields[0][dropdown][options][0][label]"="Balm sample" \
  -d "custom_fields[0][dropdown][options][0][value]"=balm \
  -d "custom_fields[0][dropdown][options][1][label]"="BB cream sample" \
  -d "custom_fields[0][dropdown][options][1][value]"=cream
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  mode: 'payment',
  ui_mode: 'embedded',
  return_url: 'https://example.com/return',
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  custom_fields: [
    {
      key: 'sample',
      label: {
        type: 'custom',
        custom: 'Free sample',
      },
      optional: true,
      type: 'dropdown',
      dropdown: {
        options: [
          {
            label: 'Balm sample',
            value: 'balm',
          },
          {
            label: 'BB cream sample',
            value: 'cream',
          },
        ],
      },
    },
  ],
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "mode": "payment",
  "ui_mode": "embedded",
  "return_url": "https://example.com/return",
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 1}],
  "custom_fields": [
    {
      "key": "sample",
      "label": {"type": "custom", "custom": "Free sample"},
      "optional": True,
      "type": "dropdown",
      "dropdown": {
        "options": [
          {"label": "Balm sample", "value": "balm"},
          {"label": "BB cream sample", "value": "cream"},
        ],
      },
    },
  ],
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'mode' => 'payment',
  'ui_mode' => 'embedded',
  'return_url' => 'https://example.com/return',
  'line_items' => [
    [
      'price' => '{{PRICE_ID}}',
      'quantity' => 1,
    ],
  ],
  'custom_fields' => [
    [
      'key' => 'sample',
      'label' => [
        'type' => 'custom',
        'custom' => 'Free sample',
      ],
      'optional' => true,
      'type' => 'dropdown',
      'dropdown' => [
        'options' => [
          [
            'label' => 'Balm sample',
            'value' => 'balm',
          ],
          [
            'label' => 'BB cream sample',
            'value' => 'cream',
          ],
        ],
      ],
    ],
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setUiMode(SessionCreateParams.UiMode.EMBEDDED)
    .setReturnUrl("https://example.com/return")
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPrice("{{PRICE_ID}}")
        .setQuantity(1L)
        .build()
    )
    .addCustomField(
      SessionCreateParams.CustomField.builder()
        .setKey("sample")
        .setLabel(
          SessionCreateParams.CustomField.Label.builder()
            .setType(SessionCreateParams.CustomField.Label.Type.CUSTOM)
            .setCustom("Free sample")
            .build()
        )
        .setOptional(true)
        .setType(SessionCreateParams.CustomField.Type.DROPDOWN)
        .setDropdown(
          SessionCreateParams.CustomField.Dropdown.builder()
            .addOption(
              SessionCreateParams.CustomField.Dropdown.Option.builder()
                .setLabel("Balm sample")
                .setValue("balm")
                .build()
            )
            .addOption(
              SessionCreateParams.CustomField.Dropdown.Option.builder()
                .setLabel("BB cream sample")
                .setValue("cream")
                .build()
            )
            .build()
        )
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session = client.v1().checkout().sessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  mode: 'payment',
  ui_mode: 'embedded',
  return_url: 'https://example.com/return',
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  custom_fields: [
    {
      key: 'sample',
      label: {
        type: 'custom',
        custom: 'Free sample',
      },
      optional: true,
      type: 'dropdown',
      dropdown: {
        options: [
          {
            label: 'Balm sample',
            value: 'balm',
          },
          {
            label: 'BB cream sample',
            value: 'cream',
          },
        ],
      },
    },
  ],
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  UIMode: stripe.String(stripe.CheckoutSessionUIModeEmbedded),
  ReturnURL: stripe.String("https://example.com/return"),
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(1),
    },
  },
  CustomFields: []*stripe.CheckoutSessionCreateCustomFieldParams{
    &stripe.CheckoutSessionCreateCustomFieldParams{
      Key: stripe.String("sample"),
      Label: &stripe.CheckoutSessionCreateCustomFieldLabelParams{
        Type: stripe.String("custom"),
        Custom: stripe.String("Free sample"),
      },
      Optional: stripe.Bool(true),
      Type: stripe.String(stripe.CheckoutSessionCustomFieldTypeDropdown),
      Dropdown: &stripe.CheckoutSessionCreateCustomFieldDropdownParams{
        Options: []*stripe.CheckoutSessionCreateCustomFieldDropdownOptionParams{
          &stripe.CheckoutSessionCreateCustomFieldDropdownOptionParams{
            Label: stripe.String("Balm sample"),
            Value: stripe.String("balm"),
          },
          &stripe.CheckoutSessionCreateCustomFieldDropdownOptionParams{
            Label: stripe.String("BB cream sample"),
            Value: stripe.String("cream"),
          },
        },
      },
    },
  },
}
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionCreateOptions
{
    Mode = "payment",
    UiMode = "embedded",
    ReturnUrl = "https://example.com/return",
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            Price = "{{PRICE_ID}}",
            Quantity = 1,
        },
    },
    CustomFields = new List<Stripe.Checkout.SessionCustomFieldOptions>
    {
        new Stripe.Checkout.SessionCustomFieldOptions
        {
            Key = "sample",
            Label = new Stripe.Checkout.SessionCustomFieldLabelOptions
            {
                Type = "custom",
                Custom = "Free sample",
            },
            Optional = true,
            Type = "dropdown",
            Dropdown = new Stripe.Checkout.SessionCustomFieldDropdownOptions
            {
                Options = new List<Stripe.Checkout.SessionCustomFieldDropdownOptionOptions>
                {
                    new Stripe.Checkout.SessionCustomFieldDropdownOptionOptions
                    {
                        Label = "Balm sample",
                        Value = "balm",
                    },
                    new Stripe.Checkout.SessionCustomFieldDropdownOptionOptions
                    {
                        Label = "BB cream sample",
                        Value = "cream",
                    },
                },
            },
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```
![A checkout page with a dropdown field](https://b.stripecdn.com/docs-statics-srv/assets/dropdown.4501d199ebe009030c2be6935cfdf2dd.png)

### Add a numbers only field 

A numbers-only field provides your customers a text field that only accepts numerical values, up to 255 digits. To create a numbers-only field, specify `type=numeric`.

#### Stripe-hosted page

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d mode=payment \
  --data-urlencode success_url="https://example.com/success" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "custom_fields[0][key]"=invoice \
  -d "custom_fields[0][label][type]"=custom \
  -d "custom_fields[0][label][custom]"="Invoice number" \
  -d "custom_fields[0][type]"=numeric
```

```cli
stripe checkout sessions create  \
  --mode=payment \
  --success-url="https://example.com/success" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "custom_fields[0][key]"=invoice \
  -d "custom_fields[0][label][type]"=custom \
  -d "custom_fields[0][label][custom]"="Invoice number" \
  -d "custom_fields[0][type]"=numeric
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  mode: 'payment',
  success_url: 'https://example.com/success',
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  custom_fields: [
    {
      key: 'invoice',
      label: {
        type: 'custom',
        custom: 'Invoice number',
      },
      type: 'numeric',
    },
  ],
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "mode": "payment",
  "success_url": "https://example.com/success",
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 1}],
  "custom_fields": [
    {
      "key": "invoice",
      "label": {"type": "custom", "custom": "Invoice number"},
      "type": "numeric",
    },
  ],
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'mode' => 'payment',
  'success_url' => 'https://example.com/success',
  'line_items' => [
    [
      'price' => '{{PRICE_ID}}',
      'quantity' => 1,
    ],
  ],
  'custom_fields' => [
    [
      'key' => 'invoice',
      'label' => [
        'type' => 'custom',
        'custom' => 'Invoice number',
      ],
      'type' => 'numeric',
    ],
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setSuccessUrl("https://example.com/success")
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPrice("{{PRICE_ID}}")
        .setQuantity(1L)
        .build()
    )
    .addCustomField(
      SessionCreateParams.CustomField.builder()
        .setKey("invoice")
        .setLabel(
          SessionCreateParams.CustomField.Label.builder()
            .setType(SessionCreateParams.CustomField.Label.Type.CUSTOM)
            .setCustom("Invoice number")
            .build()
        )
        .setType(SessionCreateParams.CustomField.Type.NUMERIC)
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session = client.v1().checkout().sessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  mode: 'payment',
  success_url: 'https://example.com/success',
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  custom_fields: [
    {
      key: 'invoice',
      label: {
        type: 'custom',
        custom: 'Invoice number',
      },
      type: 'numeric',
    },
  ],
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  SuccessURL: stripe.String("https://example.com/success"),
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(1),
    },
  },
  CustomFields: []*stripe.CheckoutSessionCreateCustomFieldParams{
    &stripe.CheckoutSessionCreateCustomFieldParams{
      Key: stripe.String("invoice"),
      Label: &stripe.CheckoutSessionCreateCustomFieldLabelParams{
        Type: stripe.String("custom"),
        Custom: stripe.String("Invoice number"),
      },
      Type: stripe.String(stripe.CheckoutSessionCustomFieldTypeNumeric),
    },
  },
}
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionCreateOptions
{
    Mode = "payment",
    SuccessUrl = "https://example.com/success",
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            Price = "{{PRICE_ID}}",
            Quantity = 1,
        },
    },
    CustomFields = new List<Stripe.Checkout.SessionCustomFieldOptions>
    {
        new Stripe.Checkout.SessionCustomFieldOptions
        {
            Key = "invoice",
            Label = new Stripe.Checkout.SessionCustomFieldLabelOptions
            {
                Type = "custom",
                Custom = "Invoice number",
            },
            Type = "numeric",
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

#### Embedded form

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d mode=payment \
  -d ui_mode=embedded \
  --data-urlencode return_url="https://example.com/return" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "custom_fields[0][key]"=invoice \
  -d "custom_fields[0][label][type]"=custom \
  -d "custom_fields[0][label][custom]"="Invoice number" \
  -d "custom_fields[0][type]"=numeric
```

```cli
stripe checkout sessions create  \
  --mode=payment \
  --ui-mode=embedded \
  --return-url="https://example.com/return" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "custom_fields[0][key]"=invoice \
  -d "custom_fields[0][label][type]"=custom \
  -d "custom_fields[0][label][custom]"="Invoice number" \
  -d "custom_fields[0][type]"=numeric
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  mode: 'payment',
  ui_mode: 'embedded',
  return_url: 'https://example.com/return',
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  custom_fields: [
    {
      key: 'invoice',
      label: {
        type: 'custom',
        custom: 'Invoice number',
      },
      type: 'numeric',
    },
  ],
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "mode": "payment",
  "ui_mode": "embedded",
  "return_url": "https://example.com/return",
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 1}],
  "custom_fields": [
    {
      "key": "invoice",
      "label": {"type": "custom", "custom": "Invoice number"},
      "type": "numeric",
    },
  ],
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'mode' => 'payment',
  'ui_mode' => 'embedded',
  'return_url' => 'https://example.com/return',
  'line_items' => [
    [
      'price' => '{{PRICE_ID}}',
      'quantity' => 1,
    ],
  ],
  'custom_fields' => [
    [
      'key' => 'invoice',
      'label' => [
        'type' => 'custom',
        'custom' => 'Invoice number',
      ],
      'type' => 'numeric',
    ],
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setUiMode(SessionCreateParams.UiMode.EMBEDDED)
    .setReturnUrl("https://example.com/return")
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPrice("{{PRICE_ID}}")
        .setQuantity(1L)
        .build()
    )
    .addCustomField(
      SessionCreateParams.CustomField.builder()
        .setKey("invoice")
        .setLabel(
          SessionCreateParams.CustomField.Label.builder()
            .setType(SessionCreateParams.CustomField.Label.Type.CUSTOM)
            .setCustom("Invoice number")
            .build()
        )
        .setType(SessionCreateParams.CustomField.Type.NUMERIC)
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session = client.v1().checkout().sessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  mode: 'payment',
  ui_mode: 'embedded',
  return_url: 'https://example.com/return',
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  custom_fields: [
    {
      key: 'invoice',
      label: {
        type: 'custom',
        custom: 'Invoice number',
      },
      type: 'numeric',
    },
  ],
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  UIMode: stripe.String(stripe.CheckoutSessionUIModeEmbedded),
  ReturnURL: stripe.String("https://example.com/return"),
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(1),
    },
  },
  CustomFields: []*stripe.CheckoutSessionCreateCustomFieldParams{
    &stripe.CheckoutSessionCreateCustomFieldParams{
      Key: stripe.String("invoice"),
      Label: &stripe.CheckoutSessionCreateCustomFieldLabelParams{
        Type: stripe.String("custom"),
        Custom: stripe.String("Invoice number"),
      },
      Type: stripe.String(stripe.CheckoutSessionCustomFieldTypeNumeric),
    },
  },
}
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionCreateOptions
{
    Mode = "payment",
    UiMode = "embedded",
    ReturnUrl = "https://example.com/return",
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            Price = "{{PRICE_ID}}",
            Quantity = 1,
        },
    },
    CustomFields = new List<Stripe.Checkout.SessionCustomFieldOptions>
    {
        new Stripe.Checkout.SessionCustomFieldOptions
        {
            Key = "invoice",
            Label = new Stripe.Checkout.SessionCustomFieldLabelOptions
            {
                Type = "custom",
                Custom = "Invoice number",
            },
            Type = "numeric",
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

### Retrieve custom fields for a subscription 

You can retrieve the custom fields associated with a subscription by querying for the Checkout Session that created it using the [subscription](https://docs.stripe.com/api/checkout/sessions/list.md#list_checkout_sessions-subscription) parameter.

```curl
curl -G https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d subscription="{{SUBSCRIPTION_ID}}"
```

```cli
stripe checkout sessions list  \
  --subscription="{{SUBSCRIPTION_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

sessions = client.v1.checkout.sessions.list({subscription: '{{SUBSCRIPTION_ID}}'})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
sessions = client.v1.checkout.sessions.list({
  "subscription": "{{SUBSCRIPTION_ID}}",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$sessions = $stripe->checkout->sessions->all([
  'subscription' => '{{SUBSCRIPTION_ID}}',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionListParams params =
  SessionListParams.builder().setSubscription("{{SUBSCRIPTION_ID}}").build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
StripeCollection<Session> stripeCollection =
  client.v1().checkout().sessions().list(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const sessions = await stripe.checkout.sessions.list({
  subscription: '{{SUBSCRIPTION_ID}}',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionListParams{
  Subscription: stripe.String("{{SUBSCRIPTION_ID}}"),
}
result := sc.V1CheckoutSessions.List(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionListOptions
{
    Subscription = "{{SUBSCRIPTION_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
StripeList<Stripe.Checkout.Session> sessions = service.List(options);
```

### Add character length validations 

You can optionally specify a minimum and maximum character length [requirement](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-custom_fields-numeric-maximum_length) for `text` and `numeric` field types.

#### Stripe-hosted page

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d mode=payment \
  --data-urlencode success_url="https://example.com/success" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "custom_fields[0][key]"=engraving \
  -d "custom_fields[0][label][type]"=custom \
  -d "custom_fields[0][label][custom]"="Personalized engraving" \
  -d "custom_fields[0][type]"=text \
  -d "custom_fields[0][text][minimum_length]"=10 \
  -d "custom_fields[0][text][maximum_length]"=20 \
  -d "custom_fields[0][optional]"=true
```

```cli
stripe checkout sessions create  \
  --mode=payment \
  --success-url="https://example.com/success" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "custom_fields[0][key]"=engraving \
  -d "custom_fields[0][label][type]"=custom \
  -d "custom_fields[0][label][custom]"="Personalized engraving" \
  -d "custom_fields[0][type]"=text \
  -d "custom_fields[0][text][minimum_length]"=10 \
  -d "custom_fields[0][text][maximum_length]"=20 \
  -d "custom_fields[0][optional]"=true
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  mode: 'payment',
  success_url: 'https://example.com/success',
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  custom_fields: [
    {
      key: 'engraving',
      label: {
        type: 'custom',
        custom: 'Personalized engraving',
      },
      type: 'text',
      text: {
        minimum_length: 10,
        maximum_length: 20,
      },
      optional: true,
    },
  ],
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "mode": "payment",
  "success_url": "https://example.com/success",
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 1}],
  "custom_fields": [
    {
      "key": "engraving",
      "label": {"type": "custom", "custom": "Personalized engraving"},
      "type": "text",
      "text": {"minimum_length": 10, "maximum_length": 20},
      "optional": True,
    },
  ],
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'mode' => 'payment',
  'success_url' => 'https://example.com/success',
  'line_items' => [
    [
      'price' => '{{PRICE_ID}}',
      'quantity' => 1,
    ],
  ],
  'custom_fields' => [
    [
      'key' => 'engraving',
      'label' => [
        'type' => 'custom',
        'custom' => 'Personalized engraving',
      ],
      'type' => 'text',
      'text' => [
        'minimum_length' => 10,
        'maximum_length' => 20,
      ],
      'optional' => true,
    ],
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setSuccessUrl("https://example.com/success")
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPrice("{{PRICE_ID}}")
        .setQuantity(1L)
        .build()
    )
    .addCustomField(
      SessionCreateParams.CustomField.builder()
        .setKey("engraving")
        .setLabel(
          SessionCreateParams.CustomField.Label.builder()
            .setType(SessionCreateParams.CustomField.Label.Type.CUSTOM)
            .setCustom("Personalized engraving")
            .build()
        )
        .setType(SessionCreateParams.CustomField.Type.TEXT)
        .setText(
          SessionCreateParams.CustomField.Text.builder()
            .setMinimumLength(10L)
            .setMaximumLength(20L)
            .build()
        )
        .setOptional(true)
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session = client.v1().checkout().sessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  mode: 'payment',
  success_url: 'https://example.com/success',
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  custom_fields: [
    {
      key: 'engraving',
      label: {
        type: 'custom',
        custom: 'Personalized engraving',
      },
      type: 'text',
      text: {
        minimum_length: 10,
        maximum_length: 20,
      },
      optional: true,
    },
  ],
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  SuccessURL: stripe.String("https://example.com/success"),
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(1),
    },
  },
  CustomFields: []*stripe.CheckoutSessionCreateCustomFieldParams{
    &stripe.CheckoutSessionCreateCustomFieldParams{
      Key: stripe.String("engraving"),
      Label: &stripe.CheckoutSessionCreateCustomFieldLabelParams{
        Type: stripe.String("custom"),
        Custom: stripe.String("Personalized engraving"),
      },
      Type: stripe.String(stripe.CheckoutSessionCustomFieldTypeText),
      Text: &stripe.CheckoutSessionCreateCustomFieldTextParams{
        MinimumLength: stripe.Int64(10),
        MaximumLength: stripe.Int64(20),
      },
      Optional: stripe.Bool(true),
    },
  },
}
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionCreateOptions
{
    Mode = "payment",
    SuccessUrl = "https://example.com/success",
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            Price = "{{PRICE_ID}}",
            Quantity = 1,
        },
    },
    CustomFields = new List<Stripe.Checkout.SessionCustomFieldOptions>
    {
        new Stripe.Checkout.SessionCustomFieldOptions
        {
            Key = "engraving",
            Label = new Stripe.Checkout.SessionCustomFieldLabelOptions
            {
                Type = "custom",
                Custom = "Personalized engraving",
            },
            Type = "text",
            Text = new Stripe.Checkout.SessionCustomFieldTextOptions
            {
                MinimumLength = 10,
                MaximumLength = 20,
            },
            Optional = true,
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

#### Embedded form

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d mode=payment \
  -d ui_mode=embedded \
  --data-urlencode return_url="https://example.com/return" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "custom_fields[0][key]"=engraving \
  -d "custom_fields[0][label][type]"=custom \
  -d "custom_fields[0][label][custom]"="Personalized engraving" \
  -d "custom_fields[0][type]"=text \
  -d "custom_fields[0][text][minimum_length]"=10 \
  -d "custom_fields[0][text][maximum_length]"=20 \
  -d "custom_fields[0][optional]"=true
```

```cli
stripe checkout sessions create  \
  --mode=payment \
  --ui-mode=embedded \
  --return-url="https://example.com/return" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "custom_fields[0][key]"=engraving \
  -d "custom_fields[0][label][type]"=custom \
  -d "custom_fields[0][label][custom]"="Personalized engraving" \
  -d "custom_fields[0][type]"=text \
  -d "custom_fields[0][text][minimum_length]"=10 \
  -d "custom_fields[0][text][maximum_length]"=20 \
  -d "custom_fields[0][optional]"=true
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  mode: 'payment',
  ui_mode: 'embedded',
  return_url: 'https://example.com/return',
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  custom_fields: [
    {
      key: 'engraving',
      label: {
        type: 'custom',
        custom: 'Personalized engraving',
      },
      type: 'text',
      text: {
        minimum_length: 10,
        maximum_length: 20,
      },
      optional: true,
    },
  ],
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "mode": "payment",
  "ui_mode": "embedded",
  "return_url": "https://example.com/return",
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 1}],
  "custom_fields": [
    {
      "key": "engraving",
      "label": {"type": "custom", "custom": "Personalized engraving"},
      "type": "text",
      "text": {"minimum_length": 10, "maximum_length": 20},
      "optional": True,
    },
  ],
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'mode' => 'payment',
  'ui_mode' => 'embedded',
  'return_url' => 'https://example.com/return',
  'line_items' => [
    [
      'price' => '{{PRICE_ID}}',
      'quantity' => 1,
    ],
  ],
  'custom_fields' => [
    [
      'key' => 'engraving',
      'label' => [
        'type' => 'custom',
        'custom' => 'Personalized engraving',
      ],
      'type' => 'text',
      'text' => [
        'minimum_length' => 10,
        'maximum_length' => 20,
      ],
      'optional' => true,
    ],
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setUiMode(SessionCreateParams.UiMode.EMBEDDED)
    .setReturnUrl("https://example.com/return")
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPrice("{{PRICE_ID}}")
        .setQuantity(1L)
        .build()
    )
    .addCustomField(
      SessionCreateParams.CustomField.builder()
        .setKey("engraving")
        .setLabel(
          SessionCreateParams.CustomField.Label.builder()
            .setType(SessionCreateParams.CustomField.Label.Type.CUSTOM)
            .setCustom("Personalized engraving")
            .build()
        )
        .setType(SessionCreateParams.CustomField.Type.TEXT)
        .setText(
          SessionCreateParams.CustomField.Text.builder()
            .setMinimumLength(10L)
            .setMaximumLength(20L)
            .build()
        )
        .setOptional(true)
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session = client.v1().checkout().sessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  mode: 'payment',
  ui_mode: 'embedded',
  return_url: 'https://example.com/return',
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  custom_fields: [
    {
      key: 'engraving',
      label: {
        type: 'custom',
        custom: 'Personalized engraving',
      },
      type: 'text',
      text: {
        minimum_length: 10,
        maximum_length: 20,
      },
      optional: true,
    },
  ],
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  UIMode: stripe.String(stripe.CheckoutSessionUIModeEmbedded),
  ReturnURL: stripe.String("https://example.com/return"),
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(1),
    },
  },
  CustomFields: []*stripe.CheckoutSessionCreateCustomFieldParams{
    &stripe.CheckoutSessionCreateCustomFieldParams{
      Key: stripe.String("engraving"),
      Label: &stripe.CheckoutSessionCreateCustomFieldLabelParams{
        Type: stripe.String("custom"),
        Custom: stripe.String("Personalized engraving"),
      },
      Type: stripe.String(stripe.CheckoutSessionCustomFieldTypeText),
      Text: &stripe.CheckoutSessionCreateCustomFieldTextParams{
        MinimumLength: stripe.Int64(10),
        MaximumLength: stripe.Int64(20),
      },
      Optional: stripe.Bool(true),
    },
  },
}
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionCreateOptions
{
    Mode = "payment",
    UiMode = "embedded",
    ReturnUrl = "https://example.com/return",
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            Price = "{{PRICE_ID}}",
            Quantity = 1,
        },
    },
    CustomFields = new List<Stripe.Checkout.SessionCustomFieldOptions>
    {
        new Stripe.Checkout.SessionCustomFieldOptions
        {
            Key = "engraving",
            Label = new Stripe.Checkout.SessionCustomFieldLabelOptions
            {
                Type = "custom",
                Custom = "Personalized engraving",
            },
            Type = "text",
            Text = new Stripe.Checkout.SessionCustomFieldTextOptions
            {
                MinimumLength = 10,
                MaximumLength = 20,
            },
            Optional = true,
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```
![A field with character limits](https://b.stripecdn.com/docs-statics-srv/assets/between-validation.20431cd8e0c03a028843945d1f1ea314.png)

### Add default values 

You can optionally provide a default value for the [text](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-custom_fields-text-default_value), [numeric](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-custom_fields-numeric-default_value), and [dropdown](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-custom_fields-dropdown-default_value) field types. Default values are prefilled on the payment page.

#### Stripe-hosted page

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d mode=payment \
  --data-urlencode success_url="https://example.com/success" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "custom_fields[0][key]"=engraving \
  -d "custom_fields[0][label][type]"=custom \
  -d "custom_fields[0][label][custom]"="Personalized engraving" \
  -d "custom_fields[0][type]"=text \
  -d "custom_fields[0][text][default_value]"=Stella \
  -d "custom_fields[1][key]"=size \
  -d "custom_fields[1][label][type]"=custom \
  -d "custom_fields[1][label][custom]"=Size \
  -d "custom_fields[1][type]"=dropdown \
  -d "custom_fields[1][dropdown][default_value]"=small \
  -d "custom_fields[1][dropdown][options][0][value]"=small \
  -d "custom_fields[1][dropdown][options][0][label]"=Small \
  -d "custom_fields[1][dropdown][options][1][value]"=large \
  -d "custom_fields[1][dropdown][options][1][label]"=Large
```

```cli
stripe checkout sessions create  \
  --mode=payment \
  --success-url="https://example.com/success" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "custom_fields[0][key]"=engraving \
  -d "custom_fields[0][label][type]"=custom \
  -d "custom_fields[0][label][custom]"="Personalized engraving" \
  -d "custom_fields[0][type]"=text \
  -d "custom_fields[0][text][default_value]"=Stella \
  -d "custom_fields[1][key]"=size \
  -d "custom_fields[1][label][type]"=custom \
  -d "custom_fields[1][label][custom]"=Size \
  -d "custom_fields[1][type]"=dropdown \
  -d "custom_fields[1][dropdown][default_value]"=small \
  -d "custom_fields[1][dropdown][options][0][value]"=small \
  -d "custom_fields[1][dropdown][options][0][label]"=Small \
  -d "custom_fields[1][dropdown][options][1][value]"=large \
  -d "custom_fields[1][dropdown][options][1][label]"=Large
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  mode: 'payment',
  success_url: 'https://example.com/success',
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  custom_fields: [
    {
      key: 'engraving',
      label: {
        type: 'custom',
        custom: 'Personalized engraving',
      },
      type: 'text',
      text: {default_value: 'Stella'},
    },
    {
      key: 'size',
      label: {
        type: 'custom',
        custom: 'Size',
      },
      type: 'dropdown',
      dropdown: {
        default_value: 'small',
        options: [
          {
            value: 'small',
            label: 'Small',
          },
          {
            value: 'large',
            label: 'Large',
          },
        ],
      },
    },
  ],
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "mode": "payment",
  "success_url": "https://example.com/success",
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 1}],
  "custom_fields": [
    {
      "key": "engraving",
      "label": {"type": "custom", "custom": "Personalized engraving"},
      "type": "text",
      "text": {"default_value": "Stella"},
    },
    {
      "key": "size",
      "label": {"type": "custom", "custom": "Size"},
      "type": "dropdown",
      "dropdown": {
        "default_value": "small",
        "options": [
          {"value": "small", "label": "Small"},
          {"value": "large", "label": "Large"},
        ],
      },
    },
  ],
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'mode' => 'payment',
  'success_url' => 'https://example.com/success',
  'line_items' => [
    [
      'price' => '{{PRICE_ID}}',
      'quantity' => 1,
    ],
  ],
  'custom_fields' => [
    [
      'key' => 'engraving',
      'label' => [
        'type' => 'custom',
        'custom' => 'Personalized engraving',
      ],
      'type' => 'text',
      'text' => ['default_value' => 'Stella'],
    ],
    [
      'key' => 'size',
      'label' => [
        'type' => 'custom',
        'custom' => 'Size',
      ],
      'type' => 'dropdown',
      'dropdown' => [
        'default_value' => 'small',
        'options' => [
          [
            'value' => 'small',
            'label' => 'Small',
          ],
          [
            'value' => 'large',
            'label' => 'Large',
          ],
        ],
      ],
    ],
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setSuccessUrl("https://example.com/success")
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPrice("{{PRICE_ID}}")
        .setQuantity(1L)
        .build()
    )
    .addCustomField(
      SessionCreateParams.CustomField.builder()
        .setKey("engraving")
        .setLabel(
          SessionCreateParams.CustomField.Label.builder()
            .setType(SessionCreateParams.CustomField.Label.Type.CUSTOM)
            .setCustom("Personalized engraving")
            .build()
        )
        .setType(SessionCreateParams.CustomField.Type.TEXT)
        .setText(
          SessionCreateParams.CustomField.Text.builder().setDefaultValue("Stella").build()
        )
        .build()
    )
    .addCustomField(
      SessionCreateParams.CustomField.builder()
        .setKey("size")
        .setLabel(
          SessionCreateParams.CustomField.Label.builder()
            .setType(SessionCreateParams.CustomField.Label.Type.CUSTOM)
            .setCustom("Size")
            .build()
        )
        .setType(SessionCreateParams.CustomField.Type.DROPDOWN)
        .setDropdown(
          SessionCreateParams.CustomField.Dropdown.builder()
            .setDefaultValue("small")
            .addOption(
              SessionCreateParams.CustomField.Dropdown.Option.builder()
                .setValue("small")
                .setLabel("Small")
                .build()
            )
            .addOption(
              SessionCreateParams.CustomField.Dropdown.Option.builder()
                .setValue("large")
                .setLabel("Large")
                .build()
            )
            .build()
        )
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session = client.v1().checkout().sessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  mode: 'payment',
  success_url: 'https://example.com/success',
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  custom_fields: [
    {
      key: 'engraving',
      label: {
        type: 'custom',
        custom: 'Personalized engraving',
      },
      type: 'text',
      text: {
        default_value: 'Stella',
      },
    },
    {
      key: 'size',
      label: {
        type: 'custom',
        custom: 'Size',
      },
      type: 'dropdown',
      dropdown: {
        default_value: 'small',
        options: [
          {
            value: 'small',
            label: 'Small',
          },
          {
            value: 'large',
            label: 'Large',
          },
        ],
      },
    },
  ],
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  SuccessURL: stripe.String("https://example.com/success"),
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(1),
    },
  },
  CustomFields: []*stripe.CheckoutSessionCreateCustomFieldParams{
    &stripe.CheckoutSessionCreateCustomFieldParams{
      Key: stripe.String("engraving"),
      Label: &stripe.CheckoutSessionCreateCustomFieldLabelParams{
        Type: stripe.String("custom"),
        Custom: stripe.String("Personalized engraving"),
      },
      Type: stripe.String(stripe.CheckoutSessionCustomFieldTypeText),
      Text: &stripe.CheckoutSessionCreateCustomFieldTextParams{
        DefaultValue: stripe.String("Stella"),
      },
    },
    &stripe.CheckoutSessionCreateCustomFieldParams{
      Key: stripe.String("size"),
      Label: &stripe.CheckoutSessionCreateCustomFieldLabelParams{
        Type: stripe.String("custom"),
        Custom: stripe.String("Size"),
      },
      Type: stripe.String(stripe.CheckoutSessionCustomFieldTypeDropdown),
      Dropdown: &stripe.CheckoutSessionCreateCustomFieldDropdownParams{
        DefaultValue: stripe.String("small"),
        Options: []*stripe.CheckoutSessionCreateCustomFieldDropdownOptionParams{
          &stripe.CheckoutSessionCreateCustomFieldDropdownOptionParams{
            Value: stripe.String("small"),
            Label: stripe.String("Small"),
          },
          &stripe.CheckoutSessionCreateCustomFieldDropdownOptionParams{
            Value: stripe.String("large"),
            Label: stripe.String("Large"),
          },
        },
      },
    },
  },
}
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionCreateOptions
{
    Mode = "payment",
    SuccessUrl = "https://example.com/success",
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            Price = "{{PRICE_ID}}",
            Quantity = 1,
        },
    },
    CustomFields = new List<Stripe.Checkout.SessionCustomFieldOptions>
    {
        new Stripe.Checkout.SessionCustomFieldOptions
        {
            Key = "engraving",
            Label = new Stripe.Checkout.SessionCustomFieldLabelOptions
            {
                Type = "custom",
                Custom = "Personalized engraving",
            },
            Type = "text",
            Text = new Stripe.Checkout.SessionCustomFieldTextOptions
            {
                DefaultValue = "Stella",
            },
        },
        new Stripe.Checkout.SessionCustomFieldOptions
        {
            Key = "size",
            Label = new Stripe.Checkout.SessionCustomFieldLabelOptions
            {
                Type = "custom",
                Custom = "Size",
            },
            Type = "dropdown",
            Dropdown = new Stripe.Checkout.SessionCustomFieldDropdownOptions
            {
                DefaultValue = "small",
                Options = new List<Stripe.Checkout.SessionCustomFieldDropdownOptionOptions>
                {
                    new Stripe.Checkout.SessionCustomFieldDropdownOptionOptions
                    {
                        Value = "small",
                        Label = "Small",
                    },
                    new Stripe.Checkout.SessionCustomFieldDropdownOptionOptions
                    {
                        Value = "large",
                        Label = "Large",
                    },
                },
            },
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

#### Embedded form

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d mode=payment \
  -d ui_mode=embedded \
  --data-urlencode return_url="https://example.com/return" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "custom_fields[0][key]"=engraving \
  -d "custom_fields[0][label][type]"=custom \
  -d "custom_fields[0][label][custom]"="Personalized engraving" \
  -d "custom_fields[0][type]"=text \
  -d "custom_fields[0][text][default_value]"=Stella \
  -d "custom_fields[1][key]"=size \
  -d "custom_fields[1][label][type]"=custom \
  -d "custom_fields[1][label][custom]"=Size \
  -d "custom_fields[1][type]"=dropdown \
  -d "custom_fields[1][dropdown][default_value]"=small \
  -d "custom_fields[1][dropdown][options][0][value]"=small \
  -d "custom_fields[1][dropdown][options][0][label]"=Small \
  -d "custom_fields[1][dropdown][options][1][value]"=large \
  -d "custom_fields[1][dropdown][options][1][label]"=Large
```

```cli
stripe checkout sessions create  \
  --mode=payment \
  --ui-mode=embedded \
  --return-url="https://example.com/return" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "custom_fields[0][key]"=engraving \
  -d "custom_fields[0][label][type]"=custom \
  -d "custom_fields[0][label][custom]"="Personalized engraving" \
  -d "custom_fields[0][type]"=text \
  -d "custom_fields[0][text][default_value]"=Stella \
  -d "custom_fields[1][key]"=size \
  -d "custom_fields[1][label][type]"=custom \
  -d "custom_fields[1][label][custom]"=Size \
  -d "custom_fields[1][type]"=dropdown \
  -d "custom_fields[1][dropdown][default_value]"=small \
  -d "custom_fields[1][dropdown][options][0][value]"=small \
  -d "custom_fields[1][dropdown][options][0][label]"=Small \
  -d "custom_fields[1][dropdown][options][1][value]"=large \
  -d "custom_fields[1][dropdown][options][1][label]"=Large
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  mode: 'payment',
  ui_mode: 'embedded',
  return_url: 'https://example.com/return',
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  custom_fields: [
    {
      key: 'engraving',
      label: {
        type: 'custom',
        custom: 'Personalized engraving',
      },
      type: 'text',
      text: {default_value: 'Stella'},
    },
    {
      key: 'size',
      label: {
        type: 'custom',
        custom: 'Size',
      },
      type: 'dropdown',
      dropdown: {
        default_value: 'small',
        options: [
          {
            value: 'small',
            label: 'Small',
          },
          {
            value: 'large',
            label: 'Large',
          },
        ],
      },
    },
  ],
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "mode": "payment",
  "ui_mode": "embedded",
  "return_url": "https://example.com/return",
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 1}],
  "custom_fields": [
    {
      "key": "engraving",
      "label": {"type": "custom", "custom": "Personalized engraving"},
      "type": "text",
      "text": {"default_value": "Stella"},
    },
    {
      "key": "size",
      "label": {"type": "custom", "custom": "Size"},
      "type": "dropdown",
      "dropdown": {
        "default_value": "small",
        "options": [
          {"value": "small", "label": "Small"},
          {"value": "large", "label": "Large"},
        ],
      },
    },
  ],
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'mode' => 'payment',
  'ui_mode' => 'embedded',
  'return_url' => 'https://example.com/return',
  'line_items' => [
    [
      'price' => '{{PRICE_ID}}',
      'quantity' => 1,
    ],
  ],
  'custom_fields' => [
    [
      'key' => 'engraving',
      'label' => [
        'type' => 'custom',
        'custom' => 'Personalized engraving',
      ],
      'type' => 'text',
      'text' => ['default_value' => 'Stella'],
    ],
    [
      'key' => 'size',
      'label' => [
        'type' => 'custom',
        'custom' => 'Size',
      ],
      'type' => 'dropdown',
      'dropdown' => [
        'default_value' => 'small',
        'options' => [
          [
            'value' => 'small',
            'label' => 'Small',
          ],
          [
            'value' => 'large',
            'label' => 'Large',
          ],
        ],
      ],
    ],
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setUiMode(SessionCreateParams.UiMode.EMBEDDED)
    .setReturnUrl("https://example.com/return")
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPrice("{{PRICE_ID}}")
        .setQuantity(1L)
        .build()
    )
    .addCustomField(
      SessionCreateParams.CustomField.builder()
        .setKey("engraving")
        .setLabel(
          SessionCreateParams.CustomField.Label.builder()
            .setType(SessionCreateParams.CustomField.Label.Type.CUSTOM)
            .setCustom("Personalized engraving")
            .build()
        )
        .setType(SessionCreateParams.CustomField.Type.TEXT)
        .setText(
          SessionCreateParams.CustomField.Text.builder().setDefaultValue("Stella").build()
        )
        .build()
    )
    .addCustomField(
      SessionCreateParams.CustomField.builder()
        .setKey("size")
        .setLabel(
          SessionCreateParams.CustomField.Label.builder()
            .setType(SessionCreateParams.CustomField.Label.Type.CUSTOM)
            .setCustom("Size")
            .build()
        )
        .setType(SessionCreateParams.CustomField.Type.DROPDOWN)
        .setDropdown(
          SessionCreateParams.CustomField.Dropdown.builder()
            .setDefaultValue("small")
            .addOption(
              SessionCreateParams.CustomField.Dropdown.Option.builder()
                .setValue("small")
                .setLabel("Small")
                .build()
            )
            .addOption(
              SessionCreateParams.CustomField.Dropdown.Option.builder()
                .setValue("large")
                .setLabel("Large")
                .build()
            )
            .build()
        )
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session = client.v1().checkout().sessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  mode: 'payment',
  ui_mode: 'embedded',
  return_url: 'https://example.com/return',
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  custom_fields: [
    {
      key: 'engraving',
      label: {
        type: 'custom',
        custom: 'Personalized engraving',
      },
      type: 'text',
      text: {
        default_value: 'Stella',
      },
    },
    {
      key: 'size',
      label: {
        type: 'custom',
        custom: 'Size',
      },
      type: 'dropdown',
      dropdown: {
        default_value: 'small',
        options: [
          {
            value: 'small',
            label: 'Small',
          },
          {
            value: 'large',
            label: 'Large',
          },
        ],
      },
    },
  ],
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  UIMode: stripe.String(stripe.CheckoutSessionUIModeEmbedded),
  ReturnURL: stripe.String("https://example.com/return"),
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(1),
    },
  },
  CustomFields: []*stripe.CheckoutSessionCreateCustomFieldParams{
    &stripe.CheckoutSessionCreateCustomFieldParams{
      Key: stripe.String("engraving"),
      Label: &stripe.CheckoutSessionCreateCustomFieldLabelParams{
        Type: stripe.String("custom"),
        Custom: stripe.String("Personalized engraving"),
      },
      Type: stripe.String(stripe.CheckoutSessionCustomFieldTypeText),
      Text: &stripe.CheckoutSessionCreateCustomFieldTextParams{
        DefaultValue: stripe.String("Stella"),
      },
    },
    &stripe.CheckoutSessionCreateCustomFieldParams{
      Key: stripe.String("size"),
      Label: &stripe.CheckoutSessionCreateCustomFieldLabelParams{
        Type: stripe.String("custom"),
        Custom: stripe.String("Size"),
      },
      Type: stripe.String(stripe.CheckoutSessionCustomFieldTypeDropdown),
      Dropdown: &stripe.CheckoutSessionCreateCustomFieldDropdownParams{
        DefaultValue: stripe.String("small"),
        Options: []*stripe.CheckoutSessionCreateCustomFieldDropdownOptionParams{
          &stripe.CheckoutSessionCreateCustomFieldDropdownOptionParams{
            Value: stripe.String("small"),
            Label: stripe.String("Small"),
          },
          &stripe.CheckoutSessionCreateCustomFieldDropdownOptionParams{
            Value: stripe.String("large"),
            Label: stripe.String("Large"),
          },
        },
      },
    },
  },
}
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionCreateOptions
{
    Mode = "payment",
    UiMode = "embedded",
    ReturnUrl = "https://example.com/return",
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            Price = "{{PRICE_ID}}",
            Quantity = 1,
        },
    },
    CustomFields = new List<Stripe.Checkout.SessionCustomFieldOptions>
    {
        new Stripe.Checkout.SessionCustomFieldOptions
        {
            Key = "engraving",
            Label = new Stripe.Checkout.SessionCustomFieldLabelOptions
            {
                Type = "custom",
                Custom = "Personalized engraving",
            },
            Type = "text",
            Text = new Stripe.Checkout.SessionCustomFieldTextOptions
            {
                DefaultValue = "Stella",
            },
        },
        new Stripe.Checkout.SessionCustomFieldOptions
        {
            Key = "size",
            Label = new Stripe.Checkout.SessionCustomFieldLabelOptions
            {
                Type = "custom",
                Custom = "Size",
            },
            Type = "dropdown",
            Dropdown = new Stripe.Checkout.SessionCustomFieldDropdownOptions
            {
                DefaultValue = "small",
                Options = new List<Stripe.Checkout.SessionCustomFieldDropdownOptionOptions>
                {
                    new Stripe.Checkout.SessionCustomFieldDropdownOptionOptions
                    {
                        Value = "small",
                        Label = "Small",
                    },
                    new Stripe.Checkout.SessionCustomFieldDropdownOptionOptions
                    {
                        Value = "large",
                        Label = "Large",
                    },
                },
            },
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```
