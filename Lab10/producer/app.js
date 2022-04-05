var AWS = require("aws-sdk");
var sns = new AWS.SNS();

const TOPIC = process.env.TOPIC;

exports.handler = async (event, context, callback) => {
  console.log("Received event: " + JSON.stringify(event, null, 2));
  console.log(`Topic : ${TOPIC}`);

  let seasonId = event.pathParameters.SEASON_ID;
  let dayId = event.pathParameters.DAY_ID;
  let resortId = event.pathParameters.RESORT_ID;
  let skierId = event.pathParameters.SKIER_ID;
  let body = JSON.parse(event.body);

  let message = {
    seasonId,
    dayId,
    resortId,
    skierId,
    time: body.time,
    liftId: body.liftID,
    waitTime: body.waitTime,
  };
  console.log("Message : " + JSON.stringify(message, null, 2));

  let snsMsg = JSON.stringify(message);
  console.log(`SNS Message : ${snsMsg}`);

  var params = {
    Message: snsMsg,
    TopicArn: TOPIC,
  };
  
  let snsRes = await sns.publish(params).promise();
  console.log(`SNS Response : ${JSON.stringify(snsRes, null, 2)}`);

  const response = {
    statusCode: 201
  };
  return response;
};
