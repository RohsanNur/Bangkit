"""Predict."""

import os
from google.cloud import automl

os.environ['GOOGLE_APPLICATION_CREDENTIALS'] = r'ServiceAccountToken.json'
project_id = "150587961926"
model_id = "ICN2716873441025196032"
file_path = "images/img5.jpg"

prediction_client = automl.PredictionServiceClient()

    # Get the full path of the model.
model_full_id = automl.AutoMlClient.model_path(project_id, "us-central1", model_id)

    # Read the file.
with open(file_path, "rb") as content_file:
  content = content_file.read()

image = automl.Image(image_bytes=content)
payload = automl.ExamplePayload(image=image)

params = {"score_threshold": "0.8"}

request = automl.PredictRequest(name=model_full_id, payload=payload, params=params)

response = prediction_client.predict(request=request)
print("Prediction results:")
for result in response.payload:
  print("Predicted class name: {}".format(result.display_name))
  print("Predicted class score: {}".format(result.image_object_detection.score))
  bounding_box = result.image_object_detection.bounding_box
  print("Normalized Vertices:")
  for vertex in bounding_box.normalized_vertices:
    print("\tX: {}, Y: {}".format(vertex.x, vertex.y))
    # [END automl_vision_object_detection_predict]
