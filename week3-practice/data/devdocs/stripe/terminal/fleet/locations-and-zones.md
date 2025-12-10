# Manage locations

Group and manage your readers by physical location.

You can streamline the management of multiple readers across different physical sites by using locations and zones.

Locations and zones help by associating each reader with specific operational sites and guarantee that the correct regional configurations are downloaded.

- **Locations**: Allows you to group readers, monitor their connectivity status, and modify your settings based on physical location. This functionality is beneficial for marketplaces with multiple connected accounts.

- **Zones**: Offers an optional method to further categorize locations and readers. Zones enable you to represent broader groups of readers or locations, such as larger geographic regions (for example, countries) or organizational sub-brands. Multiple locations can belong to a single zone, and you can create a hierarchical structure by grouping multiple zones under a single zone.

> Zones provide an additional way to group locations. You must still assign readers to a location, and you can assign a location to only one zone.
Example organization strategy (See full diagram at https://docs.stripe.com/terminal/fleet/locations-and-zones)
## Locations 

You can create a location for each physical place where your readers operate. You can [register](https://docs.stripe.com/terminal/fleet/register-readers.md) multiple readers to each location, and nest these locations within zones. Before you can use a reader, you must register it to a location.

The required [address properties](https://docs.stripe.com/api/terminal/locations/create.md#create_terminal_location-address) for a location vary by country:

### Required Address Properties

- Australia, Canada, Italy, Japan (Beta), Spain, United States: `line1`, `city`, `state`, `postal_code`, and `country` required
- Austria, Belgium, Czech Republic, Denmark, Finland, France, Germany, Luxembourg, Malaysia, Netherlands, New Zealand, Norway, Poland (Beta), Portugal, Sweden, Switzerland, United Kingdom: `line1`, `city`, `postal_code`, and `country` required
- Ireland, Singapore: `line1`, `postal_code`, and `country` required

> You can use the Dashboard or API to update a `Location` object. After you create a location, you can’t change its country. Instead, create a new location in the new country, and then re-register any readers associated with the old location.

## Zones 

Zones are the top-level groups that can consist of either more zones or locations. You can add more zones nested under an existing one, creating additional hierarchy levels, such as “West coast.” However, organizing your locations into zones is optional.

## Create locations and zones 

# Dashboard

> This is a Dashboard for when dashboard-or-api is dashboard. View the full page at https://docs.stripe.com/terminal/fleet/locations-and-zones?dashboard-or-api=dashboard.

First, you must [register your reader to a location](https://docs.stripe.com/terminal/fleet/register-readers.md?dashboard-or-api=dashboard) to accept payments. You can manage your locations and zones in the [Manage locations](https://dashboard.stripe.com/terminal/locations/manage) page. To open this page, click the **Manage locations** button on the **Locations** tab.

### Create a location

To create a location:

1. On the [Terminal Locations](https://dashboard.stripe.com/terminal/locations) page, click **Create location**
1. Enter a name and a valid address. Providing an address ensures that the correct configuration and settings are applied based on the region where the readers are operating.
1. Click **Done**.

You can also create a specific [configuration](https://docs.stripe.com/terminal/fleet/configurations-overview.md) for that location.

### Create a zone

To create a zone:

1. Click the overflow menu (⋯) on the **All locations** row, then click **Create zone**.
1. Enter a name.
1. Click **Done**.

### Create a nested zone

To create a nested zone:

1. Click the overflow menu (⋯) on the zone for which you want to create a nested zone, then click **Create zone**.
1. Enter a name.
1. Click **Done**.

### Add or move a location to a zone 

To add or move a location to a zone:

1. Click the overflow menu (⋯) on the location, then click **Move location**.
1. Choose the zone or nested zone where you want to move the location.
1. Click **Done**.

### Delete a location

To delete a location, you must remove the readers associate with it:

1. On the [Terminal Locations](https://dashboard.stripe.com/terminal/locations) page, click the overflow menu (⋯) on the location. Or, click the location and click the overflow menu button from the details page.
1. Click **Delete location**.
1. Click **Delete**.

### Delete a zone

To delete a zone, you must remove the readers associate with it:

1. Remove all readers from the location you want to delete, and remove all locations under the zones.
   - (Optional) Move the locations with readers to a different zone.
1. Click the overflow menu (⋯) on the zone, then click **Delete zone**.
1. Click **Yes, delete zone**.


# API

> This is a API for when dashboard-or-api is api. View the full page at https://docs.stripe.com/terminal/fleet/locations-and-zones?dashboard-or-api=api.

> You can’t create or modify zones using the API.

First, you must [register your reader to a location](https://docs.stripe.com/terminal/fleet/register-readers.md?dashboard-or-api=api) to accept payments.

To create a new Terminal [Location](https://docs.stripe.com/api/terminal/locations.md) object using the API, use the [create location](https://docs.stripe.com/api/terminal/locations/create.md) request.

```curl
curl https://api.stripe.com/v1/terminal/locations \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d display_name=HQ \
  -d "address[line1]"="1272 Valencia Street" \
  -d "address[city]"="San Francisco" \
  -d "address[state]"=CA \
  -d "address[country]"=US \
  -d "address[postal_code]"=94110
```

```cli
stripe terminal locations create  \
  --display-name=HQ \
  -d "address[line1]"="1272 Valencia Street" \
  -d "address[city]"="San Francisco" \
  -d "address[state]"=CA \
  -d "address[country]"=US \
  -d "address[postal_code]"=94110
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

location = client.v1.terminal.locations.create({
  display_name: 'HQ',
  address: {
    line1: '1272 Valencia Street',
    city: 'San Francisco',
    state: 'CA',
    country: 'US',
    postal_code: '94110',
  },
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
location = client.v1.terminal.locations.create({
  "display_name": "HQ",
  "address": {
    "line1": "1272 Valencia Street",
    "city": "San Francisco",
    "state": "CA",
    "country": "US",
    "postal_code": "94110",
  },
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$location = $stripe->terminal->locations->create([
  'display_name' => 'HQ',
  'address' => [
    'line1' => '1272 Valencia Street',
    'city' => 'San Francisco',
    'state' => 'CA',
    'country' => 'US',
    'postal_code' => '94110',
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

LocationCreateParams params =
  LocationCreateParams.builder()
    .setDisplayName("HQ")
    .setAddress(
      LocationCreateParams.Address.builder()
        .setLine1("1272 Valencia Street")
        .setCity("San Francisco")
        .setState("CA")
        .setCountry("US")
        .setPostalCode("94110")
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Location location = client.v1().terminal().locations().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const location = await stripe.terminal.locations.create({
  display_name: 'HQ',
  address: {
    line1: '1272 Valencia Street',
    city: 'San Francisco',
    state: 'CA',
    country: 'US',
    postal_code: '94110',
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TerminalLocationCreateParams{
  DisplayName: stripe.String("HQ"),
  Address: &stripe.AddressParams{
    Line1: stripe.String("1272 Valencia Street"),
    City: stripe.String("San Francisco"),
    State: stripe.String("CA"),
    Country: stripe.String("US"),
    PostalCode: stripe.String("94110"),
  },
}
result, err := sc.V1TerminalLocations.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Terminal.LocationCreateOptions
{
    DisplayName = "HQ",
    Address = new AddressOptions
    {
        Line1 = "1272 Valencia Street",
        City = "San Francisco",
        State = "CA",
        Country = "US",
        PostalCode = "94110",
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Terminal.Locations;
Stripe.Terminal.Location location = service.Create(options);
```

### Create a location for accounts using direct charges

To create a Location for an account using [direct charges](https://docs.stripe.com/connect/direct-charges.md), use the `Stripe-Account` header in your request. Only the Connect account you authenticate as can access these locations. If the business operates in multiple physical sites, you can create multiple Locations for any individual accounts with direct charges.

```curl
curl https://api.stripe.com/v1/terminal/locations \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d display_name=HQ \
  -d "address[line1]"="1272 Valencia Street" \
  -d "address[city]"="San Francisco" \
  -d "address[state]"=CA \
  -d "address[country]"=US \
  -d "address[postal_code]"=94110
```

```cli
stripe terminal locations create  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  --display-name=HQ \
  -d "address[line1]"="1272 Valencia Street" \
  -d "address[city]"="San Francisco" \
  -d "address[state]"=CA \
  -d "address[country]"=US \
  -d "address[postal_code]"=94110
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

location = client.v1.terminal.locations.create(
  {
    display_name: 'HQ',
    address: {
      line1: '1272 Valencia Street',
      city: 'San Francisco',
      state: 'CA',
      country: 'US',
      postal_code: '94110',
    },
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
location = client.v1.terminal.locations.create(
  {
    "display_name": "HQ",
    "address": {
      "line1": "1272 Valencia Street",
      "city": "San Francisco",
      "state": "CA",
      "country": "US",
      "postal_code": "94110",
    },
  },
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$location = $stripe->terminal->locations->create(
  [
    'display_name' => 'HQ',
    'address' => [
      'line1' => '1272 Valencia Street',
      'city' => 'San Francisco',
      'state' => 'CA',
      'country' => 'US',
      'postal_code' => '94110',
    ],
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

LocationCreateParams params =
  LocationCreateParams.builder()
    .setDisplayName("HQ")
    .setAddress(
      LocationCreateParams.Address.builder()
        .setLine1("1272 Valencia Street")
        .setCity("San Francisco")
        .setState("CA")
        .setCountry("US")
        .setPostalCode("94110")
        .build()
    )
    .build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

Location location = client.v1().terminal().locations().create(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const location = await stripe.terminal.locations.create(
  {
    display_name: 'HQ',
    address: {
      line1: '1272 Valencia Street',
      city: 'San Francisco',
      state: 'CA',
      country: 'US',
      postal_code: '94110',
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
params := &stripe.TerminalLocationCreateParams{
  DisplayName: stripe.String("HQ"),
  Address: &stripe.AddressParams{
    Line1: stripe.String("1272 Valencia Street"),
    City: stripe.String("San Francisco"),
    State: stripe.String("CA"),
    Country: stripe.String("US"),
    PostalCode: stripe.String("94110"),
  },
}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result, err := sc.V1TerminalLocations.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Terminal.LocationCreateOptions
{
    DisplayName = "HQ",
    Address = new AddressOptions
    {
        Line1 = "1272 Valencia Street",
        City = "San Francisco",
        State = "CA",
        Country = "US",
        PostalCode = "94110",
    },
};
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Terminal.Locations;
Stripe.Terminal.Location location = service.Create(options, requestOptions);
```

### Create a location for accounts using destination charges

For integrations using [destination charges](https://docs.stripe.com/connect/destination-charges.md), Locations belong to the *platform* account and aren’t mapped strictly to connected accounts. If your platform needs to associate accounts using destination charges with Locations, you can store a reference to the relevant accounts in the Location’s [metadata](https://docs.stripe.com/api/terminal/locations/object.md#terminal_location_object-metadata) property.

#### curl

```bash
curl https://api.stripe.com/v1/terminal/locations \
  -u <<YOUR_SECRET_KEY>>: \
  -d "display_name"="HQ" \
  -d "address[line1]"="1272 Valencia Street" \
  -d "address[city]"="San Francisco" \
  -d "address[state]"="CA" \
  -d "address[country]"="US" \
  -d "address[postal_code]"="94110" \-d "metadata[connected_account]"="{{CONNECTED_ACCOUNT_ID}}"
```

#### Ruby

```ruby

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
Stripe.api_key = '<<YOUR_SECRET_KEY>>'

Stripe::Terminal::Location.create({
  display_name: 'HQ',
  address: {
    line1: '1272 Valencia Street',
    city: 'San Francisco',
    state: 'CA',
    country: 'US',
    postal_code: '94110',
  },metadata: {
    connected_account: '{{CONNECTED_ACCOUNT_ID}}',
  }
})
```

#### Python

```python

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
stripe.api_key = '<<YOUR_SECRET_KEY>>'

stripe.terminal.Location.create(
  display_name='HQ',
  address={
    'line1': '1272 Valencia Street',
    'city': 'San Francisco',
    'state': 'CA',
    'country': 'US',
    'postal_code': '94110',
  },metadata={
    'connected_account': '{{CONNECTED_ACCOUNT_ID}}',
  }
)
```

#### PHP

```php

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
\Stripe\Stripe::setApiKey('<<YOUR_SECRET_KEY>>');

\Stripe\Terminal\Location::create([
  'display_name' => 'HQ',
  'address' => [
    'line1' => '1272 Valencia Street',
    'city' => 'San Francisco',
    'state' => 'CA',
    'country' => 'US',
    'postal_code' => '94110',
  ],'metadata' => [
    'connected_account' => '{{CONNECTED_ACCOUNT_ID}}',
  ]
])
```

#### Java

```java

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
Stripe.apiKey = "<<YOUR_SECRET_KEY>>";

LocationCreateParams.Address address =
  LocationCreateParams.Address.builder()
    .setLine1("1272 Valencia Street")
    .setCity("San Francisco")
    .setState("CA")
    .setCountry("US")
    .setPostalCode("94110")
    .build();

LocationCreateParams params =
  LocationCreateParams.builder()
    .setDisplayName("HQ")
    .setAddress(address).putMetadata("connected_account", ""{{CONNECTED_ACCOUNT_ID}}"")
    .build();

Location.create(params);
```

#### Node.js

```javascript

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const location = await stripe.terminal.locations.create({
  display_name: 'HQ',
  address: {
    line1: '1272 Valencia Street',
    city: 'San Francisco',
    state: 'CA',
    country: 'US',
    postal_code: '94110',
  },metadata: {
    connected_account: '{{CONNECTED_ACCOUNT_ID}}'
  }
})
```

#### Go

```go

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
stripe.Key = "<<YOUR_SECRET_KEY>>"

params := &stripe.TerminalLocationParams{
  DisplayName: stripe.String("HQ"),
  Address: &stripe.AccountAddressParams{
    Line1: stripe.String("1272 Valencia Street"),
    City: stripe.String("San Francisco"),
    State: stripe.String("CA"),
    Country: stripe.String("US"),
    PostalCode: stripe.String("94110"),
  },
}
params.AddMetadata("connected_account", ""{{CONNECTED_ACCOUNT_ID}}"")

l, _ := location.New(params)
```

#### .NET

```csharp

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeConfiguration.ApiKey = "<<YOUR_SECRET_KEY>>";

var options = new LocationCreateOptions
{
  DisplayName = "HQ",
  Address = new AddressOptions
  {
    Line1 = "1272 Valencia Street",
    City = "San Francisco",
    State = "CA",
    Country = "US",
    PostalCode = "94110",Metadata = new Dictionary<string, string> {
      { "connected_account", "{{CONNECTED_ACCOUNT_ID}}" },
    }
  },
};

var service = new LocationService();
service.Create(options);
```

When you [register your reader to a Location](https://docs.stripe.com/terminal/fleet/register-readers.md), the specified Location groups the reader and defines its country settings.

## Scope connection tokens (Server-side) (Smart readers) 

When creating a [ConnectionToken](https://docs.stripe.com/api/terminal/connection_tokens.md) for the Terminal SDK, you can provide a `location` parameter to control access to smart readers. If you provide a Location, the ConnectionToken is only usable with smart readers assigned to that Location. If you don’t provide a Location, the ConnectionToken is usable with all readers.

> For Bluetooth readers, the `location` of a ConnectionToken has no effect. This ensures that any nearby Bluetooth readers remain discoverable.

```curl
curl https://api.stripe.com/v1/terminal/connection_tokens \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d location="{{TERMINALLOCATION_ID}}"
```

```cli
stripe terminal connection_tokens create  \
  --location="{{TERMINALLOCATION_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

connection_token = client.v1.terminal.connection_tokens.create({
  location: '{{TERMINALLOCATION_ID}}',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
connection_token = client.v1.terminal.connection_tokens.create({
  "location": "{{TERMINALLOCATION_ID}}",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$connectionToken = $stripe->terminal->connectionTokens->create([
  'location' => '{{TERMINALLOCATION_ID}}',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

ConnectionTokenCreateParams params =
  ConnectionTokenCreateParams.builder()
    .setLocation("{{TERMINALLOCATION_ID}}")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
ConnectionToken connectionToken =
  client.v1().terminal().connectionTokens().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const connectionToken = await stripe.terminal.connectionTokens.create({
  location: '{{TERMINALLOCATION_ID}}',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TerminalConnectionTokenCreateParams{
  Location: stripe.String("{{TERMINALLOCATION_ID}}"),
}
result, err := sc.V1TerminalConnectionTokens.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Terminal.ConnectionTokenCreateOptions
{
    Location = "{{TERMINALLOCATION_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Terminal.ConnectionTokens;
Stripe.Terminal.ConnectionToken connectionToken = service.Create(options);
```

