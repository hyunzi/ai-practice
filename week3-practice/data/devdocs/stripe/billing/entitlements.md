# Entitlements

Determine when you can grant or revoke product feature access to customers.

Entitlements enable you to map the features of your internal service to Stripe products. After you map your features, Stripe notifies you about when to provision or de-provision access (according to your customer?�s subscription status), and to what features, based on your mapping choices.

Use Entitlements to:

- Launch, change, and experiment with your pricing without needing to change your codebase
- Grant, revoke, and manage your customer?�s feature access
- Simplify your billing integration
![](https://d37ugbyn3rpeym.cloudfront.net/videos/entitlements-demo.mp4)
## Before you begin

This guide assumes that you?�re already using Stripe [Subscriptions](https://docs.stripe.com/billing/subscriptions/build-subscriptions.md) and [Customer](https://docs.stripe.com/invoicing/customer.md) resources.

# Dashboard

> This is a Dashboard for when dashboard-or-api is dashboard. View the full page at https://docs.stripe.com/billing/entitlements?dashboard-or-api=dashboard.

## Get started 

To get started with Entitlements:

- **Set up your features**: Create each feature in Stripe Billing from the Dashboard. Here are some examples of features you can include:
  - API access
  - AI assistant
  - Premium support
  - Advanced reporting
  - Extended data retention
- **Add your features to products**: Attach features to corresponding Stripe Products. You can add a single feature to multiple products.
- **Manage your features**: Edit or archive each feature from the Dashboard.

## Set up your features

To create a feature in the Dashboard, do the following:

1. In the Dashboard, go to the [Product catalog](https://dashboard.stripe.com/products) and click **Features**.
1. Click **+ Create feature** and enter a Name and a Lookup Key for the feature. You can optionally also add Metadata.
1. Because the Lookup Key is unique to each feature, you can?�t reuse it across different features unless you archive the feature associated with the Lookup Key.
1. Click **Create feature**.

## Add your features to products

To add a feature to a product in the Dashboard, do the following:

1. In the Dashboard, from the [Features](https://dashboard.stripe.com/features) tab, click the feature that you want to add.
1. Click **Attach to product** and select a product from the menu.
1. Click **Confirm**.

When a customer subscribes to the product, you can view what features they?�re entitled to. To do this, go to the [Customers](https://dashboard.stripe.com/customers) page, select the customer, and review the Entitlements section.

> Existing subscriptions will create active entitlements for any product feature changes at the start of the next billing period.

## Manage features

You can manage features from the Dashboard.

### Edit a feature

To edit a feature?�s Name or add Metadata, go to the [Features](https://dashboard.stripe.com/features) tab, click the overflow menu (??, and click **Edit feature**. You can?�t edit a feature?�s Lookup Key after the feature is created.

### Remove a feature from a product

To remove a feature from a product, go to the [Features](https://dashboard.stripe.com/features) tab and select the feature. Then click the overflow menu (?? next to the product name and click **Remove product**.

### Archive a feature

To archive a feature, go to the [Features](https://dashboard.stripe.com/features) tab, click the overflow menu (??, and click **Archive feature**.

Before you archive a feature, keep in mind the following:

- Archived features can?�t be edited or added to any new products.
- Archived features still create entitlements if attached to existing products.
- An archived feature?�s lookup key can be used again.
- You can?�t unarchive a feature.


# API

> This is a API for when dashboard-or-api is api. View the full page at https://docs.stripe.com/billing/entitlements?dashboard-or-api=api.

## Get started 

To get started with Entitlements:

- **Set up your features**: Create each feature in Stripe Billing using the [Feature API](https://docs.stripe.com/api/entitlements/feature/object.md). Here are some examples of features you can include:
  - API access
  - AI assistant
  - Premium support
  - Advanced reporting
  - Extended data retention
- **Add your features to products**: Attach Features to corresponding Stripe Products. You can add a single feature to multiple products.
- **Get customers??active entitlements**: When customers subscribe to your products, Stripe Billing entitles the customer to the product?�s features. Listen to the [Active Entitlement Summary webhook](https://docs.stripe.com/billing/entitlements.md#webhooks) and use the [List Active Entitlements API](https://docs.stripe.com/api/entitlements/active-entitlement/list.md) for a given customer to execute your feature provisioning process.
![Diagram with Entitlements and its relationship with a Customer and Product's features](https://b.stripecdn.com/docs-statics-srv/assets/entitlements-diagram.240f8f8017776fc513dd69f291daed0d.png)

## Set up your features

Provide a name and a unique `lookup_key` for each feature you create. Because the `lookup_key` is unique to each feature, you can?�t reuse it across different features.

```curl
curl https://api.stripe.com/v1/entitlements/features \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d name="My feature" \
  -d lookup_key=myinternalfeaturecode
```

```cli
stripe entitlements features create  \
  --name="My feature" \
  --lookup-key=myinternalfeaturecode
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

feature = client.v1.entitlements.features.create({
  name: 'My feature',
  lookup_key: 'myinternalfeaturecode',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
feature = client.v1.entitlements.features.create({
  "name": "My feature",
  "lookup_key": "myinternalfeaturecode",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$feature = $stripe->entitlements->features->create([
  'name' => 'My feature',
  'lookup_key' => 'myinternalfeaturecode',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

FeatureCreateParams params =
  FeatureCreateParams.builder()
    .setName("My feature")
    .setLookupKey("myinternalfeaturecode")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Feature feature = client.v1().entitlements().features().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const feature = await stripe.entitlements.features.create({
  name: 'My feature',
  lookup_key: 'myinternalfeaturecode',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.EntitlementsFeatureCreateParams{
  Name: stripe.String("My feature"),
  LookupKey: stripe.String("myinternalfeaturecode"),
}
result, err := sc.V1EntitlementsFeatures.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Entitlements.FeatureCreateOptions
{
    Name = "My feature",
    LookupKey = "myinternalfeaturecode",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Entitlements.Features;
Stripe.Entitlements.Feature feature = service.Create(options);
```

## Add your features to products

Assign your feature to one or more products.

> Existing subscriptions will create active entitlements for any product feature changes at the start of the next billing period.

```curl
curl https://api.stripe.com/v1/products/{{PRODUCT_ID}}/features \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d entitlement_feature="{{ENTITLEMENTSFEATURE_ID}}"
```

```cli
stripe product_features create {{PRODUCT_ID}} \
  --entitlement-feature="{{ENTITLEMENTSFEATURE_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

product_feature = client.v1.products.features.create(
  '{{PRODUCT_ID}}',
  {entitlement_feature: '{{ENTITLEMENTSFEATURE_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
product_feature = client.v1.products.features.create(
  "{{PRODUCT_ID}}",
  {"entitlement_feature": "{{ENTITLEMENTSFEATURE_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$productFeature = $stripe->products->createFeature(
  '{{PRODUCT_ID}}',
  ['entitlement_feature' => '{{ENTITLEMENTSFEATURE_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

ProductFeatureCreateParams params =
  ProductFeatureCreateParams.builder()
    .setEntitlementFeature("{{ENTITLEMENTSFEATURE_ID}}")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
ProductFeature productFeature =
  client.v1().products().features().create("{{PRODUCT_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const productFeature = await stripe.products.createFeature(
  '{{PRODUCT_ID}}',
  {
    entitlement_feature: '{{ENTITLEMENTSFEATURE_ID}}',
  }
);
```

```go

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
stripe.Key = "<<YOUR_SECRET_KEY>>"

params := &stripe.ProductFeatureParams{
  Product: stripe.String("{{PRODUCT_ID}}"),
  EntitlementFeature: stripe.String("{{FEATURE_ID}}"),
};
result := productfeature.New(params);
```

```dotnet

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeConfiguration.ApiKey = "<<YOUR_SECRET_KEY>>";

var options = new ProductFeatureCreateOptions { Product = "{{PRODUCT_ID}}", EntitlementFeature = "{{FEATURE_ID}}" };
var service = new ProductFeatureService();
service.Create(options);
```

After you submit a request to attach your feature to your product, you receive a response similar to the following:

```json
{
  "id": "{{PRODUCT_FEATURE_ID}}",
  "object": "product_feature",
  "entitlement_feature": {
    "id": "{{ENTITLEMENTS_FEATURE_ID}}",
    "object": "entitlements.feature",
    "name": "My feature",
    "lookup_key": "myinternalfeaturecode"
  }
}
```

List the features attached to a product by paging through the list of product features:

```curl
curl https://api.stripe.com/v1/products/{{PRODUCT_ID}}/features \
  -u "<<YOUR_SECRET_KEY>>:"
```

```cli
stripe product_features list {{PRODUCT_ID}}
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

product_features = client.v1.products.features.list('{{PRODUCT_ID}}')
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
product_features = client.v1.products.features.list("{{PRODUCT_ID}}")
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$productFeatures = $stripe->products->allFeatures('{{PRODUCT_ID}}', []);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

ProductFeatureListParams params = ProductFeatureListParams.builder().build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
StripeCollection<ProductFeature> stripeCollection =
  client.v1().products().features().list("{{PRODUCT_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const productFeatures = await stripe.products.listFeatures('{{PRODUCT_ID}}');
```

```go

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
stripe.Key = "<<YOUR_SECRET_KEY>>"

params := &stripe.ProductFeatureListParams{
  Product: stripe.String("{{PRODUCT_ID}}")
};
result := productfeature.List(params);
```

```dotnet

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeConfiguration.ApiKey = "<<YOUR_SECRET_KEY>>";

var options = new ProductFeatureListOptions { Product = "{{PRODUCT_ID}}" };
var service = new ProductFeatureService();
StripeList<ProductFeature> productFeatures = service.List(options);
```

And remove a feature from a specific product by deleting the product feature attachment:

```curl
curl -X DELETE https://api.stripe.com/v1/products/{{PRODUCT_ID}}/features/{{PRODUCTFEATURE_ID}} \
  -u "<<YOUR_SECRET_KEY>>:"
```

```cli
stripe product_features delete {{PRODUCT_ID}} {{PRODUCTFEATURE_ID}}
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

deleted = client.v1.products.features.delete(
  '{{PRODUCT_ID}}',
  '{{PRODUCTFEATURE_ID}}',
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
deleted = client.v1.products.features.delete(
  "{{PRODUCT_ID}}",
  "{{PRODUCTFEATURE_ID}}",
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$deleted = $stripe->products->deleteFeature(
  '{{PRODUCT_ID}}',
  '{{PRODUCTFEATURE_ID}}',
  []
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
ProductFeature productFeature =
  client.v1().products().features().delete(
    "{{PRODUCT_ID}}",
    "{{PRODUCTFEATURE_ID}}"
  );
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const deleted = await stripe.products.deleteFeature(
  '{{PRODUCT_ID}}',
  '{{PRODUCTFEATURE_ID}}'
);
```

```go

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
stripe.Key = "<<YOUR_SECRET_KEY>>"

params := &stripe.ProductFeatureParams{};
result := productfeature.Del("{{PRODUCT_FEATURE_ID}}", params);
```

```dotnet

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeConfiguration.ApiKey = "<<YOUR_SECRET_KEY>>";

var service = new ProductFeatureService();
service.Delete("{{PRODUCT_FEATURE_ID}}");
```

## Get customers' active entitlements

During the lifecycle of a customer?�s subscription, from activation, through upgrades, downgrades, and so on, Stripe updates the customer?�s entitlements based on your mapped features.

When a customer?�s subscription is first activated, Stripe creates entitlements for the features that they?�re subscribed to.

As long as a customer maintains an active subscription for a feature, they retain an active entitlement. Make sure you provision access in your system for any users entitled to this feature.

### Listen for webhook events 

If your webhooks are enabled, we send the following webhook event to notify you when a customer?�s entitlements change.

> #### Limited entitlements available in summary webhook
> 
> The entitlement summary?�s `active_entitlements` property only contains a maximum of 10 entitlements. We also provide a URL to retrieve the full, paginated list of the customer?�s entitlements.

| Event                                             | Description                                               |
| ------------------------------------------------- | --------------------------------------------------------- |
| `entitlements.active_entitlement_summary.updated` | Occurs each time a customer?�s active entitlements change. |

### Retrieve the list of all active entitlements for a customer 

The list endpoint returns paginated view of a customer?�s active entitlements.

```curl
curl -G https://api.stripe.com/v1/entitlements/active_entitlements \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer="{{CUSTOMER_ID}}"
```

```cli
stripe entitlements active_entitlements list  \
  --customer="{{CUSTOMER_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

active_entitlements = client.v1.entitlements.active_entitlements.list({
  customer: '{{CUSTOMER_ID}}',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
active_entitlements = client.v1.entitlements.active_entitlements.list({
  "customer": "{{CUSTOMER_ID}}",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$activeEntitlements = $stripe->entitlements->activeEntitlements->all([
  'customer' => '{{CUSTOMER_ID}}',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

ActiveEntitlementListParams params =
  ActiveEntitlementListParams.builder().setCustomer("{{CUSTOMER_ID}}").build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
StripeCollection<ActiveEntitlement> stripeCollection =
  client.v1().entitlements().activeEntitlements().list(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const activeEntitlements = await stripe.entitlements.activeEntitlements.list({
  customer: '{{CUSTOMER_ID}}',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.EntitlementsActiveEntitlementListParams{
  Customer: stripe.String("{{CUSTOMER_ID}}"),
}
result := sc.V1EntitlementsActiveEntitlements.List(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Entitlements.ActiveEntitlementListOptions
{
    Customer = "{{CUSTOMER_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Entitlements.ActiveEntitlements;
StripeList<Stripe.Entitlements.ActiveEntitlement> activeEntitlements = service.List(
    options);
```

> #### Recommendation
> 
> We recommend you persist these entitlements internally for faster resolution.

> Subscription pricing, plan, and entitlement changes might be subject to certain legal requirements. Consult with your legal counsel for advice specific to your business.

