# Use the Registrations API to manage tax registrations

Learn how to add, schedule, and check active tax registrations.

Businesses are required to register to collect taxes in locations where they have tax obligations. The [Tax Registration API](https://docs.stripe.com/api/tax/registrations.md) lets you retrieve and configure tax registrations using an API instead of the Dashboard. Adding your registrations in Stripe turns on tax calculation and collection for your transactions made through Stripe.

Different rules determine when and how a business needs to register to collect tax depending on the location. You can also use [Stripe to register](https://docs.stripe.com/tax/use-stripe-to-register.md) on your behalf or do it yourself. [Learn more about tax collection in different locations](https://docs.stripe.com/tax/supported-countries.md).

- **Connect platform**: As a platform, you can use this API to manage the registrations of your connected accounts or to check an account’s active registrations.
- **Direct usage**: You can use this API to manage and check your registrations.

# Connect platform

> This is a Connect platform for when tax-integration is connect-platform. View the full page at https://docs.stripe.com/tax/registrations-api?tax-integration=connect-platform.

## List all tax registrations for your connected accounts 

To get a list of your connected accounts’ tax registrations, make a [list registrations](https://docs.stripe.com/api/tax/registrations/all.md) call. You can filter the response by setting the `status` parameter to `active`, `expired`, or `scheduled`.

```curl
curl -G https://api.stripe.com/v1/tax/registrations \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d status=active \
  -d limit=3
```

```cli
stripe tax registrations list  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  --status=active \
  --limit=3
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

registrations = client.v1.tax.registrations.list(
  {
    status: 'active',
    limit: 3,
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
registrations = client.v1.tax.registrations.list(
  {"status": "active", "limit": 3},
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$registrations = $stripe->tax->registrations->all(
  [
    'status' => 'active',
    'limit' => 3,
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

RegistrationListParams params =
  RegistrationListParams.builder()
    .setStatus(RegistrationListParams.Status.ACTIVE)
    .setLimit(3L)
    .build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

StripeCollection<Registration> stripeCollection =
  client.v1().tax().registrations().list(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const registrations = await stripe.tax.registrations.list(
  {
    status: 'active',
    limit: 3,
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
params := &stripe.TaxRegistrationListParams{
  Status: stripe.String(stripe.TaxRegistrationStatusActive),
}
params.Limit = stripe.Int64(3)
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result := sc.V1TaxRegistrations.List(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Tax.RegistrationListOptions { Status = "active", Limit = 3 };
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Tax.Registrations;
StripeList<Stripe.Tax.Registration> registrations = service.List(options, requestOptions);
```

If your connected accounts don’t have access to the Stripe Dashboard, your platform can provide a UI for them to manage their tax registrations. The registrations endpoint helps you implement that functionality.

## Add a tax registration for your connected account 

If a tax obligation and registration of the connected account is known to the platform, you can perform a [create registration](https://docs.stripe.com/api/tax/registrations/create.md) call using the `Stripe-Account` header with a value of the connected account ID to add or schedule the registration for the connected account.

```curl
curl https://api.stripe.com/v1/tax/registrations \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d country=IE \
  -d "country_options[ie][type]"=oss_union \
  -d active_from=now
```

```cli
stripe tax registrations create  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  --country=IE \
  -d "country_options[ie][type]"=oss_union \
  --active-from=now
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

registration = client.v1.tax.registrations.create(
  {
    country: 'IE',
    country_options: {ie: {type: 'oss_union'}},
    active_from: 'now',
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
registration = client.v1.tax.registrations.create(
  {
    "country": "IE",
    "country_options": {"ie": {"type": "oss_union"}},
    "active_from": "now",
  },
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$registration = $stripe->tax->registrations->create(
  [
    'country' => 'IE',
    'country_options' => ['ie' => ['type' => 'oss_union']],
    'active_from' => 'now',
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

RegistrationCreateParams params =
  RegistrationCreateParams.builder()
    .setCountry("IE")
    .setCountryOptions(
      RegistrationCreateParams.CountryOptions.builder()
        .setIe(
          RegistrationCreateParams.CountryOptions.Ie.builder()
            .setType(RegistrationCreateParams.CountryOptions.Ie.Type.OSS_UNION)
            .build()
        )
        .build()
    )
    .setActiveFrom(RegistrationCreateParams.ActiveFrom.NOW)
    .build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

Registration registration =
  client.v1().tax().registrations().create(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const registration = await stripe.tax.registrations.create(
  {
    country: 'IE',
    country_options: {
      ie: {
        type: 'oss_union',
      },
    },
    active_from: 'now',
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
params := &stripe.TaxRegistrationCreateParams{
  Country: stripe.String("IE"),
  CountryOptions: &stripe.TaxRegistrationCreateCountryOptionsParams{
    Ie: &stripe.TaxRegistrationCreateCountryOptionsIeParams{
      Type: stripe.String(stripe.TaxRegistrationCountryOptionsIeTypeOssUnion),
    },
  },
  ActiveFromNow: stripe.Bool(true),
}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result, err := sc.V1TaxRegistrations.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Tax.RegistrationCreateOptions
{
    Country = "IE",
    CountryOptions = new Stripe.Tax.RegistrationCountryOptionsOptions
    {
        Ie = new Stripe.Tax.RegistrationCountryOptionsIeOptions { Type = "oss_union" },
    },
    ActiveFrom = Stripe.Tax.RegistrationActiveFrom.Now,
};
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Tax.Registrations;
Stripe.Tax.Registration registration = service.Create(options, requestOptions);
```

In this case, a `tax.registration` object is created in the connected account.

```json
{
  "object": "tax.registration",
  "active_from": 1669249440,
  "country": "IE",
  "country_options": {
    "ie": {
      "type": "oss_union"
    }
  },
  "livemode": false,
  "status": "active",
  ...
}
```

Alternatively, for connected accounts with access to the Stripe Dashboard (for example, Standard accounts), you can instruct them to [set up Stripe Tax](https://docs.stripe.com/tax/set-up.md) using the Dashboard, which includes adding tax registrations.

### Head office address requirement

To add a tax registration, the connected account must first set up a head office address. Without a defined head office address, an `invalid_request_error` gets triggered with a message about setting your head office address.

Use the [tax settings API](https://docs.stripe.com/tax/settings-api.md) to add a head office address as a platform:

```curl
curl https://api.stripe.com/v1/tax/settings \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d "head_office[address][country]"=DE
```

```cli
stripe tax settings update  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  -d "head_office[address][country]"=DE
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

settings = client.v1.tax.settings.update(
  {head_office: {address: {country: 'DE'}}},
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
settings = client.v1.tax.settings.update(
  {"head_office": {"address": {"country": "DE"}}},
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$settings = $stripe->tax->settings->update(
  ['head_office' => ['address' => ['country' => 'DE']]],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SettingsUpdateParams params =
  SettingsUpdateParams.builder()
    .setHeadOffice(
      SettingsUpdateParams.HeadOffice.builder()
        .setAddress(
          SettingsUpdateParams.HeadOffice.Address.builder().setCountry("DE").build()
        )
        .build()
    )
    .build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

Settings settings = client.v1().tax().settings().update(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const settings = await stripe.tax.settings.update(
  {
    head_office: {
      address: {
        country: 'DE',
      },
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
params := &stripe.TaxSettingsUpdateParams{
  HeadOffice: &stripe.TaxSettingsUpdateHeadOfficeParams{
    Address: &stripe.AddressParams{Country: stripe.String("DE")},
  },
}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result, err := sc.V1TaxSettings.Update(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Tax.SettingsUpdateOptions
{
    HeadOffice = new Stripe.Tax.SettingsHeadOfficeOptions
    {
        Address = new AddressOptions { Country = "DE" },
    },
};
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Tax.Settings;
Stripe.Tax.Settings settings = service.Update(options, requestOptions);
```

Location-specific validation and errors might occur, details of which are found in our [tax settings guide](https://docs.stripe.com/tax/settings-api.md?tax-integration=connect-platform#validations-and-errors).

## Update and expire a tax registration for your connected account 

You can’t delete a registration after it’s created, but you can end it by setting `expires_at` to a time when the registration is no longer active. Update the registrations with an [update registration](https://docs.stripe.com/api/tax/registrations/update.md) call using the Stripe-Account header with a value of the connected account ID:

```curl
curl https://api.stripe.com/v1/tax/registrations/taxreg_NkyGPRPytKq66j \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d expires_at=now
```

```cli
stripe tax registrations update taxreg_NkyGPRPytKq66j \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  --expires-at=now
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

registration = client.v1.tax.registrations.update(
  'taxreg_NkyGPRPytKq66j',
  {expires_at: 'now'},
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
registration = client.v1.tax.registrations.update(
  "taxreg_NkyGPRPytKq66j",
  {"expires_at": "now"},
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$registration = $stripe->tax->registrations->update(
  'taxreg_NkyGPRPytKq66j',
  ['expires_at' => 'now'],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

RegistrationUpdateParams params =
  RegistrationUpdateParams.builder()
    .setExpiresAt(RegistrationUpdateParams.ExpiresAt.NOW)
    .build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

Registration registration =
  client.v1().tax().registrations().update(
    "taxreg_NkyGPRPytKq66j",
    params,
    requestOptions
  );
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const registration = await stripe.tax.registrations.update(
  'taxreg_NkyGPRPytKq66j',
  {
    expires_at: 'now',
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
params := &stripe.TaxRegistrationUpdateParams{ExpiresAtNow: stripe.Bool(true)}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result, err := sc.V1TaxRegistrations.Update(
  context.TODO(), "taxreg_NkyGPRPytKq66j", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Tax.RegistrationUpdateOptions
{
    ExpiresAt = Stripe.Tax.RegistrationExpiresAt.Now,
};
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Tax.Registrations;
Stripe.Tax.Registration registration = service.Update(
    "taxreg_NkyGPRPytKq66j",
    options,
    requestOptions);
```

In this case, the registration expires immediately. [Tax calculations](https://docs.stripe.com/api/tax/calculations.md) performed for the connected account after the `expires_at` won’t use this registration.

```json
{
  "object": "tax.registration",
  "active_from": 1669248000,
  "created": 1669219200,"expires_at": 1669334400,
  "livemode": false,
  "status": "active",
  ...
}
```

## Optional: Add a tax registration for the retail delivery fee [Server-side]

To calculate the retail delivery fee for your connected account, you need to add a tax registration in Colorado, United States, or in Minnesota, United States, with the registration type `state_retail_delivery_fee`. You must add this registration to your connected account.

```curl
curl https://api.stripe.com/v1/tax/registrations \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d country=US \
  -d "country_options[us][state]"=CO \
  -d "country_options[us][type]"=state_retail_delivery_fee \
  -d active_from=now
```

```cli
stripe tax registrations create  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  --country=US \
  -d "country_options[us][state]"=CO \
  -d "country_options[us][type]"=state_retail_delivery_fee \
  --active-from=now
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

registration = client.v1.tax.registrations.create(
  {
    country: 'US',
    country_options: {
      us: {
        state: 'CO',
        type: 'state_retail_delivery_fee',
      },
    },
    active_from: 'now',
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
registration = client.v1.tax.registrations.create(
  {
    "country": "US",
    "country_options": {"us": {"state": "CO", "type": "state_retail_delivery_fee"}},
    "active_from": "now",
  },
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$registration = $stripe->tax->registrations->create(
  [
    'country' => 'US',
    'country_options' => [
      'us' => [
        'state' => 'CO',
        'type' => 'state_retail_delivery_fee',
      ],
    ],
    'active_from' => 'now',
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

RegistrationCreateParams params =
  RegistrationCreateParams.builder()
    .setCountry("US")
    .setCountryOptions(
      RegistrationCreateParams.CountryOptions.builder()
        .setUs(
          RegistrationCreateParams.CountryOptions.Us.builder()
            .setState("CO")
            .setType(
              RegistrationCreateParams.CountryOptions.Us.Type.STATE_RETAIL_DELIVERY_FEE
            )
            .build()
        )
        .build()
    )
    .setActiveFrom(RegistrationCreateParams.ActiveFrom.NOW)
    .build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

Registration registration =
  client.v1().tax().registrations().create(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const registration = await stripe.tax.registrations.create(
  {
    country: 'US',
    country_options: {
      us: {
        state: 'CO',
        type: 'state_retail_delivery_fee',
      },
    },
    active_from: 'now',
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
params := &stripe.TaxRegistrationCreateParams{
  Country: stripe.String("US"),
  CountryOptions: &stripe.TaxRegistrationCreateCountryOptionsParams{
    US: &stripe.TaxRegistrationCreateCountryOptionsUSParams{
      State: stripe.String("CO"),
      Type: stripe.String(stripe.TaxRegistrationCountryOptionsUSTypeStateRetailDeliveryFee),
    },
  },
  ActiveFromNow: stripe.Bool(true),
}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result, err := sc.V1TaxRegistrations.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Tax.RegistrationCreateOptions
{
    Country = "US",
    CountryOptions = new Stripe.Tax.RegistrationCountryOptionsOptions
    {
        Us = new Stripe.Tax.RegistrationCountryOptionsUsOptions
        {
            State = "CO",
            Type = "state_retail_delivery_fee",
        },
    },
    ActiveFrom = Stripe.Tax.RegistrationActiveFrom.Now,
};
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Tax.Registrations;
Stripe.Tax.Registration registration = service.Create(options, requestOptions);
```

After adding the tax registration, the retail delivery fee applies to tax calculations if the item sold meets the requirements of the different states. The fee applies in Minnesota, for example, on orders above 100 USD.

Refer to the documentation to check if the retail delivery fee applies:

- [Retail Delivery Fee—Colorado](https://docs.stripe.com/tax/supported-countries/united-states/colorado.md#other-taxes)
- [Retail Delivery Fee—Minnesota](https://docs.stripe.com/tax/supported-countries/united-states/minnesota.md#other-taxes)

## See also

- [Use the Settings API to configure Stripe Tax](https://docs.stripe.com/tax/settings-api.md)
- [Use Stripe Tax with Connect](https://docs.stripe.com/tax/connect.md)


# Direct usage

> This is a Direct usage for when tax-integration is direct. View the full page at https://docs.stripe.com/tax/registrations-api?tax-integration=direct.

## Add a tax registration 

You can perform a [create registration](https://docs.stripe.com/api/tax/registrations/create.md) call to add or schedule your registration:

```curl
curl https://api.stripe.com/v1/tax/registrations \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d country=IE \
  -d "country_options[ie][type]"=oss_union \
  -d active_from=now
```

```cli
stripe tax registrations create  \
  --country=IE \
  -d "country_options[ie][type]"=oss_union \
  --active-from=now
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

registration = client.v1.tax.registrations.create({
  country: 'IE',
  country_options: {ie: {type: 'oss_union'}},
  active_from: 'now',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
registration = client.v1.tax.registrations.create({
  "country": "IE",
  "country_options": {"ie": {"type": "oss_union"}},
  "active_from": "now",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$registration = $stripe->tax->registrations->create([
  'country' => 'IE',
  'country_options' => ['ie' => ['type' => 'oss_union']],
  'active_from' => 'now',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

RegistrationCreateParams params =
  RegistrationCreateParams.builder()
    .setCountry("IE")
    .setCountryOptions(
      RegistrationCreateParams.CountryOptions.builder()
        .setIe(
          RegistrationCreateParams.CountryOptions.Ie.builder()
            .setType(RegistrationCreateParams.CountryOptions.Ie.Type.OSS_UNION)
            .build()
        )
        .build()
    )
    .setActiveFrom(RegistrationCreateParams.ActiveFrom.NOW)
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Registration registration = client.v1().tax().registrations().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const registration = await stripe.tax.registrations.create({
  country: 'IE',
  country_options: {
    ie: {
      type: 'oss_union',
    },
  },
  active_from: 'now',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TaxRegistrationCreateParams{
  Country: stripe.String("IE"),
  CountryOptions: &stripe.TaxRegistrationCreateCountryOptionsParams{
    Ie: &stripe.TaxRegistrationCreateCountryOptionsIeParams{
      Type: stripe.String(stripe.TaxRegistrationCountryOptionsIeTypeOssUnion),
    },
  },
  ActiveFromNow: stripe.Bool(true),
}
result, err := sc.V1TaxRegistrations.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Tax.RegistrationCreateOptions
{
    Country = "IE",
    CountryOptions = new Stripe.Tax.RegistrationCountryOptionsOptions
    {
        Ie = new Stripe.Tax.RegistrationCountryOptionsIeOptions { Type = "oss_union" },
    },
    ActiveFrom = Stripe.Tax.RegistrationActiveFrom.Now,
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Tax.Registrations;
Stripe.Tax.Registration registration = service.Create(options);
```

In this case, Stripe creates a `tax.registration` object:

```json
{
  "object": "tax.registration",
  "active_from": 1669249440,
  "country": "IE",
  "country_options": {
    "ie": {
      "type": "oss_union"
    }
  },
  "livemode": false,
  "status": "active",
  ...
}
```

### Head office address requirement

To add a tax registration, you must first set up a head office address. Without a defined head office address, an `invalid_request_error` gets triggered with a message about setting your head office address.

Use the [tax settings API](https://docs.stripe.com/tax/settings-api.md) to add a head office address:

```curl
curl https://api.stripe.com/v1/tax/settings \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "head_office[address][country]"=DE
```

```cli
stripe tax settings update  \
  -d "head_office[address][country]"=DE
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

settings = client.v1.tax.settings.update({head_office: {address: {country: 'DE'}}})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
settings = client.v1.tax.settings.update({"head_office": {"address": {"country": "DE"}}})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$settings = $stripe->tax->settings->update([
  'head_office' => ['address' => ['country' => 'DE']],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SettingsUpdateParams params =
  SettingsUpdateParams.builder()
    .setHeadOffice(
      SettingsUpdateParams.HeadOffice.builder()
        .setAddress(
          SettingsUpdateParams.HeadOffice.Address.builder().setCountry("DE").build()
        )
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Settings settings = client.v1().tax().settings().update(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const settings = await stripe.tax.settings.update({
  head_office: {
    address: {
      country: 'DE',
    },
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TaxSettingsUpdateParams{
  HeadOffice: &stripe.TaxSettingsUpdateHeadOfficeParams{
    Address: &stripe.AddressParams{Country: stripe.String("DE")},
  },
}
result, err := sc.V1TaxSettings.Update(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Tax.SettingsUpdateOptions
{
    HeadOffice = new Stripe.Tax.SettingsHeadOfficeOptions
    {
        Address = new AddressOptions { Country = "DE" },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Tax.Settings;
Stripe.Tax.Settings settings = service.Update(options);
```

Location-specific validation and errors might occur, details of which are found in our [tax settings guide](https://docs.stripe.com/tax/settings-api.md?tax-integration=direct#validations-and-errors).

## Update and expire a tax registration 

You can’t delete a registration after it’s created, but you can end it by setting `expires_at` to a time when the registration is no longer active. Update the registrations with an [update registration](https://docs.stripe.com/api/tax/registrations/update.md) call:

```curl
curl https://api.stripe.com/v1/tax/registrations/taxreg_NkyGPRPytKq66j \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d expires_at=now
```

```cli
stripe tax registrations update taxreg_NkyGPRPytKq66j \
  --expires-at=now
```

```ruby
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

registration = client.v1.tax.registrations.update(
  'taxreg_NkyGPRPytKq66j',
  {expires_at: 'now'},
)
```

```python
client = StripeClient("<<YOUR_SECRET_KEY>>")

registration = client.v1.tax.registrations.update(
  "taxreg_NkyGPRPytKq66j",
  {"expires_at": "now"},
)
```

```php
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$registration = $stripe->tax->registrations->update(
  'taxreg_NkyGPRPytKq66j',
  ['expires_at' => 'now']
);
```

```java
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

RegistrationUpdateParams params =
  RegistrationUpdateParams.builder()
    .setExpiresAt(RegistrationUpdateParams.ExpiresAt.NOW)
    .build();

Registration registration =
  client.v1().tax().registrations().update("taxreg_NkyGPRPytKq66j", params);
```

```node
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const registration = await stripe.tax.registrations.update(
  'taxreg_NkyGPRPytKq66j',
  {
    expires_at: 'now',
  }
);
```

```go
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TaxRegistrationUpdateParams{ExpiresAtNow: stripe.Bool(true)}
result, err := sc.V1TaxRegistrations.Update(
  context.TODO(), "taxreg_NkyGPRPytKq66j", params)
```

```dotnet
var options = new Stripe.Tax.RegistrationUpdateOptions
{
    ExpiresAt = Stripe.Tax.RegistrationExpiresAt.Now,
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Tax.Registrations;
Stripe.Tax.Registration registration = service.Update(
    "taxreg_NkyGPRPytKq66j",
    options);
```

In this case, the registration expires immediately. [Tax calculations](https://docs.stripe.com/api/tax/calculations.md) performed after the `expires_at` won’t use this registration:

```json
{
  "object": "tax.registration",
  "active_from": 1669248000,
  "created": 1669219200,"expires_at": 1669334400,
  "livemode": false,
  "status": "active",
  ...
}
```

## List all your tax registrations 

You can display all your tax registrations performing a [list registrations](https://docs.stripe.com/api/tax/registrations.md) call. The `status` parameter allows you to filter for only `active`, `expired`, or `scheduled` tax registrations:

```curl
curl -G https://api.stripe.com/v1/tax/registrations \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d status=active \
  -d limit=3
```

```cli
stripe tax registrations list  \
  --status=active \
  --limit=3
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

registrations = client.v1.tax.registrations.list({
  status: 'active',
  limit: 3,
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
registrations = client.v1.tax.registrations.list({"status": "active", "limit": 3})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$registrations = $stripe->tax->registrations->all([
  'status' => 'active',
  'limit' => 3,
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

RegistrationListParams params =
  RegistrationListParams.builder()
    .setStatus(RegistrationListParams.Status.ACTIVE)
    .setLimit(3L)
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
StripeCollection<Registration> stripeCollection =
  client.v1().tax().registrations().list(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const registrations = await stripe.tax.registrations.list({
  status: 'active',
  limit: 3,
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TaxRegistrationListParams{
  Status: stripe.String(stripe.TaxRegistrationStatusActive),
}
params.Limit = stripe.Int64(3)
result := sc.V1TaxRegistrations.List(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Tax.RegistrationListOptions { Status = "active", Limit = 3 };
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Tax.Registrations;
StripeList<Stripe.Tax.Registration> registrations = service.List(options);
```

## Optional: Add a tax registration for the retail delivery fee [Server-side]

To calculate the retail delivery fee, you need to add a tax registration in Colorado, United States, or in Minnesota, United States, with the registration type `state_retail_delivery_fee`.

```curl
curl https://api.stripe.com/v1/tax/registrations \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d country=US \
  -d "country_options[us][state]"=CO \
  -d "country_options[us][type]"=state_retail_delivery_fee \
  -d active_from=now
```

```cli
stripe tax registrations create  \
  --country=US \
  -d "country_options[us][state]"=CO \
  -d "country_options[us][type]"=state_retail_delivery_fee \
  --active-from=now
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

registration = client.v1.tax.registrations.create({
  country: 'US',
  country_options: {
    us: {
      state: 'CO',
      type: 'state_retail_delivery_fee',
    },
  },
  active_from: 'now',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
registration = client.v1.tax.registrations.create({
  "country": "US",
  "country_options": {"us": {"state": "CO", "type": "state_retail_delivery_fee"}},
  "active_from": "now",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$registration = $stripe->tax->registrations->create([
  'country' => 'US',
  'country_options' => [
    'us' => [
      'state' => 'CO',
      'type' => 'state_retail_delivery_fee',
    ],
  ],
  'active_from' => 'now',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

RegistrationCreateParams params =
  RegistrationCreateParams.builder()
    .setCountry("US")
    .setCountryOptions(
      RegistrationCreateParams.CountryOptions.builder()
        .setUs(
          RegistrationCreateParams.CountryOptions.Us.builder()
            .setState("CO")
            .setType(
              RegistrationCreateParams.CountryOptions.Us.Type.STATE_RETAIL_DELIVERY_FEE
            )
            .build()
        )
        .build()
    )
    .setActiveFrom(RegistrationCreateParams.ActiveFrom.NOW)
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Registration registration = client.v1().tax().registrations().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const registration = await stripe.tax.registrations.create({
  country: 'US',
  country_options: {
    us: {
      state: 'CO',
      type: 'state_retail_delivery_fee',
    },
  },
  active_from: 'now',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TaxRegistrationCreateParams{
  Country: stripe.String("US"),
  CountryOptions: &stripe.TaxRegistrationCreateCountryOptionsParams{
    US: &stripe.TaxRegistrationCreateCountryOptionsUSParams{
      State: stripe.String("CO"),
      Type: stripe.String(stripe.TaxRegistrationCountryOptionsUSTypeStateRetailDeliveryFee),
    },
  },
  ActiveFromNow: stripe.Bool(true),
}
result, err := sc.V1TaxRegistrations.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Tax.RegistrationCreateOptions
{
    Country = "US",
    CountryOptions = new Stripe.Tax.RegistrationCountryOptionsOptions
    {
        Us = new Stripe.Tax.RegistrationCountryOptionsUsOptions
        {
            State = "CO",
            Type = "state_retail_delivery_fee",
        },
    },
    ActiveFrom = Stripe.Tax.RegistrationActiveFrom.Now,
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Tax.Registrations;
Stripe.Tax.Registration registration = service.Create(options);
```

After adding the tax registration, the retail delivery fee applies to tax calculations if the item sold meets the requirements of the different states. The fee applies in Minnesota, for example, on orders above 100 USD.

Refer to the documentation to check if the retail delivery fee applies:

- [Retail Delivery Fee—Colorado](https://docs.stripe.com/tax/supported-countries/united-states/colorado.md#other-taxes)
- [Retail Delivery Fee—Minnesota](https://docs.stripe.com/tax/supported-countries/united-states/minnesota.md#other-taxes)

## See also

- [Use the Settings API to configure Stripe Tax](https://docs.stripe.com/tax/settings-api.md)
- [Use Stripe to register](https://docs.stripe.com/tax/use-stripe-to-register.md)

