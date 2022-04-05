const dynamodb = require("aws-sdk/clients/dynamodb");
const docClient = new dynamodb.DocumentClient();

const TABLE_NAME = "sam-milestone-SkierTable-7OYPGU414DLY" //process.env.TABLE_NAME;

exports.handler = async (event) => {

    console.log(event);

    console.log(`Inserting ${TABLE_NAME}`);

    const data = JSON.stringify(event);
    let time = new Date().toISOString();
    var params = {
        TableName: TABLE_NAME,
        Item: { symbol : time ,message : data},
        Key : { symbol : time}
      };
    
      // Call DynamoDB to add the item to the table
      let result = await docClient.put(params).promise();
      // result.then(function(data) {
      //   console.log("Success", data);
      // }).catch(function(err) {
      //   console.log("Error", err);
      // });

      // , function(err, data) {
      //   if (err) {
      //     console.log("Error", err);
      //   } else {
      //     console.log("Success", data);
      //   }
      // });
    

      console.log(result)
      // console.log(temp2)

    return "OK";
  }
