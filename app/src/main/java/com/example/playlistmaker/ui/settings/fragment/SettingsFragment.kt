package com.example.playlistmaker.ui.settings.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.settings.viewmodel.SettingsViewModel
import com.example.playlistmaker.ui.settings.viewmodel.model.SettingsConstants
import com.example.playlistmaker.ui.util.theme.AppDimens
import com.example.playlistmaker.ui.util.theme.AppTextStyles
import com.example.playlistmaker.ui.util.theme.AppTheme
import com.example.playlistmaker.ui.util.theme.colors
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val settingsViewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppTheme(isSystemInDarkTheme()) {
                    SettingsScreenContent(
                        viewModel = settingsViewModel,
                        onShareClick = { settingsViewModel.share() },
                        onSupportClick = { settingsViewModel.contactSupport() },
                        onUserAgreementClick = { settingsViewModel.openUserAgreement() }
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingsViewModel.setConstants(SettingsConstants(requireContext()))
    }
}

@Composable
fun SettingsScreenContent(
    viewModel: SettingsViewModel,
    onShareClick: () -> Unit,
    onSupportClick: () -> Unit,
    onUserAgreementClick: () -> Unit
) {
    val isDarkTheme by viewModel.observeDarkTheme().observeAsState(isSystemInDarkTheme())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors().colorSecondary)
    ) {
        Text(
            text = stringResource(R.string.settings_btn),
            style = AppTextStyles.mediumText,
            color = colors().colorOnSecondary,
            modifier = Modifier
                .padding(
                    start = AppDimens.normalPadding,
                    end = AppDimens.normalPadding,
                    top = AppDimens.normalPadding,
                    bottom = AppDimens.minorPadding
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = AppDimens.normalPadding)
        ) {
            SettingsSwitchItem(
                text = stringResource(R.string.dark_theme),
                checked = isDarkTheme,
                onCheckedChange = { isChecked ->
                    viewModel.changeDarkTheme(isChecked)
                }
            )

            SettingsButtonItem(
                text = stringResource(R.string.share_app),
                icon = R.drawable.share,
                onClick = onShareClick
            )

            SettingsButtonItem(
                text = stringResource(R.string.contact_support),
                icon = R.drawable.support,
                onClick = onSupportClick
            )

            SettingsButtonItem(
                text = stringResource(R.string.user_agreement),
                icon = R.drawable.mini_arrow,
                onClick = onUserAgreementClick
            )
        }
    }
}

@Composable
fun SettingsSwitchItem(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val colors = colors()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            // Свитч в композе себя ведет странно, поэтому пришлось захардкодить его высоту чтобы
            // она была такой же как и остальные
            .height(AppDimens.settingsVerticalButtonPadding * 2 + AppDimens.settingsIconSize)
            .clickable { onCheckedChange(!checked) }
            .padding(
                horizontal = AppDimens.normalPadding
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            style = AppTextStyles.regularText,
            color = colors.colorOnSecondary
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = colors.blue,
                checkedTrackColor = colors.lightBlue,
                uncheckedThumbColor = colors.gray,
                uncheckedTrackColor = colors.lightGray
            )
        )
    }
}

@Composable
fun SettingsButtonItem(
    text: String,
    icon: Int,
    onClick: () -> Unit
) {
    val colors = colors()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(
                horizontal = AppDimens.normalPadding,
                vertical = AppDimens.settingsVerticalButtonPadding
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            style = AppTextStyles.regularText,
            color = colors.colorOnSecondary,
            modifier = Modifier.weight(1f)
        )

        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = colors.grayToWhite,
            modifier = Modifier.size(AppDimens.settingsIconSize)
        )
    }
}