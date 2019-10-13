import com.bhailaverse.mirror.model.WeatherData;
import com.bhailaverse.mirror.service.DarkskyWeatherService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.mockito.Mockito.anyString;
import static org.powermock.api.mockito.PowerMockito.*;

import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({WebClient.class})
public class DarkskyWeatherServiceTest {
    @Test
    public void testForSuccess() throws Exception {


        WebClient webclient = PowerMockito.mock(WebClient.class);
        mockStatic(WebClient.class);
        WebClient.Builder builder = PowerMockito.mock(WebClient.Builder.class);

        when(WebClient.builder()).thenReturn(builder);
        when(builder.baseUrl(anyString())).thenReturn(builder);
        when(builder.defaultHeader(anyString(), anyString())).thenReturn(builder);
        when(builder.build()).thenReturn(webclient);

        DarkskyWeatherService darkskyWeatherService = new DarkskyWeatherService("http://localhost", "test_api_key");

        WebClient.RequestHeadersUriSpec headersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        Mono<String> mono = Mono.just(new String(Files.readAllBytes(Paths.get("src/test/resources/sample_weather_response.json"))));
        when(webclient.get()).thenReturn(headersUriSpec);
        when(headersUriSpec.uri(anyString(), anyString(), anyString())).thenReturn(headersUriSpec);
        when(headersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(mono);

        WeatherData data = darkskyWeatherService.getForecast("somelat", "somelng").block();
        assertTrue(data != null);
    }
}
